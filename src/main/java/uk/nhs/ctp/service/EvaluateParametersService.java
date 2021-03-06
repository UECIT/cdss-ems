package uk.nhs.ctp.service;

import com.google.common.base.Preconditions;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.Iterables;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Stream;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hl7.fhir.dstu3.model.CodeableConcept;
import org.hl7.fhir.dstu3.model.Coding;
import org.hl7.fhir.dstu3.model.IdType;
import org.hl7.fhir.dstu3.model.Immunization;
import org.hl7.fhir.dstu3.model.MedicationAdministration;
import org.hl7.fhir.dstu3.model.MedicationAdministration.MedicationAdministrationStatus;
import org.hl7.fhir.dstu3.model.Meta;
import org.hl7.fhir.dstu3.model.Observation;
import org.hl7.fhir.dstu3.model.Parameters;
import org.hl7.fhir.dstu3.model.Parameters.ParametersParameterComponent;
import org.hl7.fhir.dstu3.model.QuestionnaireResponse;
import org.hl7.fhir.dstu3.model.Reference;
import org.hl7.fhir.dstu3.model.Resource;
import org.hl7.fhir.dstu3.model.ResourceType;
import org.hl7.fhir.dstu3.model.StringType;
import org.hl7.fhir.instance.model.api.IIdType;
import org.springframework.stereotype.Service;
import uk.nhs.ctp.SystemConstants;
import uk.nhs.ctp.SystemURL;
import uk.nhs.ctp.entities.CaseImmunization;
import uk.nhs.ctp.entities.CaseMedication;
import uk.nhs.ctp.entities.CaseParameter;
import uk.nhs.ctp.entities.Cases;
import uk.nhs.ctp.enums.Language;
import uk.nhs.ctp.enums.Setting;
import uk.nhs.ctp.enums.UserType;
import uk.nhs.ctp.repos.CaseRepository;
import uk.nhs.ctp.service.dto.CodeDTO;
import uk.nhs.ctp.service.dto.SettingsDTO;
import uk.nhs.ctp.service.dto.TriageQuestion;
import uk.nhs.ctp.service.fhir.ReferenceService;
import uk.nhs.ctp.transform.ObservationTransformer;
import uk.nhs.ctp.utils.ErrorHandlingUtils;

@Service
@AllArgsConstructor
@Slf4j
public class EvaluateParametersService {

  private static Set<String> validInitiatingPersonTypes =
      Set.of("Patient", "RelatedPerson", "Practitioner");
  private static Set<String> validReceivingPersonTypes =
      Set.of("Patient", "RelatedPerson");

  private CaseRepository caseRepository;
  private AuditService auditService;
  private ReferenceService referenceService;
  private ObservationTransformer observationTransformer;
  private QuestionnaireService questionnaireService;

  Parameters getEvaluateParameters(
      Long caseId,
      TriageQuestion[] questionResponse,
      SettingsDTO settings,
      Boolean amending,
      String questionnaireId,
      String supplierBaseUrl,
      String requestId
  ) {
    Cases caseEntity = caseRepository.findOne(caseId);

    ErrorHandlingUtils.checkEntityExists(caseEntity, "Case");
    Builder builder = new Builder()
        .setRequestId(requestId)
        .setEncounter(caseId)
        .setPatient(new Reference(caseEntity.getPatientId()))
        .setSetting(settings.getSetting());

    addPeople(builder, caseEntity.getPatientId(), settings);
    addInputData(caseEntity, builder);
    addInputParameters(caseEntity, builder);
    addQuestionnaireResponses(builder, caseEntity, questionnaireId, questionResponse, amending, supplierBaseUrl);

    return builder.build();
  }

  private void addInputData(Cases caseEntity, Builder builder) {
    getObservations(caseEntity).forEach(builder::addInputData);
    getImmunizations(caseEntity).forEach(builder::addInputData);
    getMedications(caseEntity).forEach(builder::addInputData);
  }

  private void addPeople(Builder builder, String patientId, SettingsDTO settings) {

    Setting setting = Setting.fromCode(settings.getSetting().getCode());
    // Settings of phone call or face to face imply the practitioner is the initiating person
    UserType initiatingType = setting == Setting.ONLINE
        ? UserType.fromCode(settings.getUserType().getCode())
        : UserType.PRACTITIONER;

    UserType receivingType = initiatingType != UserType.PRACTITIONER
        ? initiatingType
        : UserType.fromCode(settings.getUserType().getCode());

    Reference patientRef = new Reference(patientId);
    //Assume RelatedPerson.id = patient.id
    Reference relatedPersonRef = referenceService.buildRef(ResourceType.RelatedPerson, new IdType(patientId).getIdPart());

    builder
        .setUserType(initiatingType)
        .setRecipientType(receivingType)
        .setUserLanguage(settings.getUserLanguage())
        .setRecipientLanguage(settings.getRecipientLanguage())
        .setUserTaskContext(settings.getUserTaskContext());

    switch (initiatingType) {
      case PATIENT:
        builder.setInitiatingAndReceiving(patientRef);
        break;
      case RELATED_PERSON:
        // TODO get related person from frontend
        builder.setInitiatingAndReceiving(relatedPersonRef);
        break;
      case PRACTITIONER:
        Preconditions.checkNotNull(settings.getPractitioner(), "No practitioner specified");
        builder.setInitiatingPerson(
            referenceService.buildRef(ResourceType.Practitioner, settings.getPractitioner().getId()));
        builder.setReceivingPerson(UserType.PATIENT.equals(receivingType) ? patientRef : relatedPersonRef);
        break;
      default:
        throw new IllegalStateException("Unexpected value: " + initiatingType);
    }
  }

  private void addQuestionnaireResponses(
      Builder builder,
      Cases caseEntity,
      String questionnaireId,
      TriageQuestion[] questionResponse,
      Boolean amending,
      String supplierBaseUrl
  ) {
    Reference responseSource = (Reference)builder.getUnique(SystemConstants.RECEIVINGPERSON).getValue();
    List<QuestionnaireResponse> questionnaireResponses = questionnaireService
        .updateEncounterResponses(
            caseEntity, questionnaireId, questionResponse, amending, responseSource,
            supplierBaseUrl);
    builder.addQuestionnaireResponses(questionnaireResponses);
  }

  private ArrayList<Immunization> getImmunizations(Cases caseEntity) {
    // TODO support sending as reference or resource - NCTH-450
    ArrayList<Immunization> immunizations = new ArrayList<>();

    if (!caseEntity.getImmunizations().isEmpty()) {
      for (CaseImmunization immunizationEntity : caseEntity.getImmunizations()) {
        Immunization immunization = new Immunization()
            .setStatus(Immunization.ImmunizationStatus.COMPLETED)
            .setVaccineCode(new CodeableConcept().addCoding(new Coding(SystemURL.SNOMED,
                immunizationEntity.getCode(), immunizationEntity.getDisplay())))
            .setNotGiven(immunizationEntity.getNotGiven());

        immunizations.add(immunization);
      }
    }

    return immunizations;
  }

  private ArrayList<MedicationAdministration> getMedications(Cases caseEntity) {
    // TODO support sending as reference or resource - NCTH-450
    ArrayList<MedicationAdministration> medications = new ArrayList<>();
    for (CaseMedication medicationEntity : caseEntity.getMedications()) {
      MedicationAdministration medication = new MedicationAdministration()
          .setStatus(MedicationAdministrationStatus.COMPLETED)
          .setMedication(new CodeableConcept().addCoding(new Coding(SystemURL.SNOMED,
              medicationEntity.getCode(), medicationEntity.getDisplay())))
          .setNotGiven(medicationEntity.getNotGiven());

      medications.add(medication);
    }

    return medications;
  }

  private Stream<Observation> getObservations(Cases caseEntity) {
    // TODO support sending as reference or resource - NCTH-450
//    return caseEntity.getObservations().stream()
//        .map(o -> referenceService.buildRef(ResourceType.Observation, o.getId()));
    return caseEntity.getObservations().stream()
        .map(observationTransformer::transform);
  }

  private void addInputParameters(Cases caseEntity, Builder builder) {
    // TODO review this in more detail. Non outputData output parameters are included here
    if (!caseEntity.getParameters().isEmpty()) {
      Parameters inputParameters = new Parameters();
      builder.addParameter(SystemConstants.INPUT_PARAMETERS)
          .setResource(inputParameters);

      for (CaseParameter parameterEntity : caseEntity.getParameters()) {
        inputParameters.addParameter()
            .setName(parameterEntity.getName())
            .setValue(new StringType(parameterEntity.getValue()));
      }
    }
  }

  private class Builder {

    private final HashMultimap<String, ParametersParameterComponent> parameterNames;
    private final Parameters parameters;

    public Builder() {
      parameters = new Parameters();
      parameters.setMeta(new Meta().addProfile(SystemURL.SERVICE_DEFINITION_EVALUATE));
      parameterNames = HashMultimap.create();
    }

    public Parameters build() {
      validate();
      return parameters;
    }

    public void validate() {
      validate(SystemConstants.REQUEST_ID)
          .checkSingle()
          .checkType(IIdType.class);

      for (ParametersParameterComponent parameter : parameterNames
          .get(SystemConstants.INPUT_DATA)) {
        if (parameter.getResource() != null) {
          log.warn("Found embedded resource in inputData of type {}",
              parameter.getResource().getResourceType());
        }
      }

      validate(SystemConstants.PATIENT)
          .checkSingle()
          .checkReferenceType(ResourceType.Patient);

      validate(SystemConstants.ENCOUNTER)
          .checkSingle()
          .checkReferenceType(ResourceType.Encounter);

      validate(SystemConstants.INITIATINGPERSON)
          .checkSingle()
          .checkReferenceType(ResourceType.Patient, ResourceType.RelatedPerson,
              ResourceType.Practitioner);

      validate(SystemConstants.USERTYPE)
          .checkSingle()
          .checkCodeableConcept(validInitiatingPersonTypes);

      validate(SystemConstants.RECEIVINGPERSON)
          .checkSingle()
          .checkReferenceType(ResourceType.Patient, ResourceType.RelatedPerson);

      validate(SystemConstants.SETTING)
          .checkSingle();

      validate("settingContext")
          .checkAbsent();
    }

    private Validator<Set<ParametersParameterComponent>> validate(String parameter) {
      return new Validator<>(parameterNames.get(parameter), parameter);
    }

    private ParametersParameterComponent addParameter(String name) {
      ParametersParameterComponent param = parameters.addParameter().setName(name);
      parameterNames.put(name, param);
      return param;
    }

    private ParametersParameterComponent addUniqueParameter(String name) {
      Preconditions.checkArgument(!parameterNames.containsKey(name),
          "Parameter " + name + " must be unique");
      return addParameter(name);
    }

    private CodeableConcept toSnomedCode(CodeDTO typeCodeDTO) {
      return new CodeableConcept()
          .setText(typeCodeDTO.getDisplay())
          .addCoding(new Coding()
              .setCode(typeCodeDTO.getCode())
              .setDisplay(typeCodeDTO.getDisplay())
              .setSystem(SystemURL.SNOMED));
    }

    public Builder setRequestId(String requestId) {
      addUniqueParameter(SystemConstants.REQUEST_ID)
          .setValue(new IdType(requestId));
      return this;
    }

    public Builder setEncounter(Long caseId) {
      addUniqueParameter(SystemConstants.ENCOUNTER)
          .setValue(referenceService.buildRef(ResourceType.Encounter, caseId));

      return this;
    }

    public Builder setPatient(Reference patient) {
      Preconditions.checkArgument(patient.getReferenceElement().getResourceType().equals("Patient"),
          "Reference must be of type Patient");

      addUniqueParameter(SystemConstants.PATIENT)
          .setValue(patient);
      return this;
    }

    public Builder addQuestionnaireResponses(List<QuestionnaireResponse> questionnaireResponses) {
      for (QuestionnaireResponse resource : questionnaireResponses) {
        addParameter(SystemConstants.INPUT_DATA)
            .setResource(resource);
      }
      return this;
    }

    public Builder setInitiatingPerson(Reference reference) {
      addUniqueParameter(SystemConstants.INITIATINGPERSON)
          .setValue(reference);
      return this;
    }

    public Builder setReceivingPerson(Reference reference) {
      addUniqueParameter(SystemConstants.RECEIVINGPERSON)
          .setValue(reference);

      return this;
    }

    public Builder setInitiatingAndReceiving(Reference reference) {
      return setInitiatingPerson(reference).setReceivingPerson(reference);
    }

    public Builder setUserType(UserType userType) {
      Preconditions.checkArgument(
          validInitiatingPersonTypes.contains(userType.getValue()),
          "User type must be one of " + validInitiatingPersonTypes);

      addUniqueParameter(SystemConstants.USERTYPE)
          .setValue(userType.toCodeableConcept());

      return this;
    }

    public Builder setRecipientType(UserType recipientType) {
      Preconditions.checkArgument(
          validReceivingPersonTypes.contains(recipientType.getValue()),
          "Recipient type must be one of " + validReceivingPersonTypes);

      addUniqueParameter(SystemConstants.RECIPIENTTYPE)
          .setValue(recipientType.toCodeableConcept());

      return this;
    }

    public Builder setUserLanguage(CodeDTO languageDTO) {
      CodeableConcept language =
          Language.fromCode(languageDTO.getCode()).toCodeableConcept();

      addUniqueParameter(SystemConstants.USERLANGUAGE)
          .setValue(language);

      return this;
    }

    public Builder setRecipientLanguage(CodeDTO languageDTO) {
      CodeableConcept language =
          Language.fromCode(languageDTO.getCode()).toCodeableConcept();

      addUniqueParameter(SystemConstants.RECIPIENTLANGUAGE)
          .setValue(language);

      return this;
    }

    public Builder setUserTaskContext(CodeDTO contextDTO) {
      CodeableConcept context = toSnomedCode(contextDTO);

      addUniqueParameter(SystemConstants.USERTASKCONTEXT)
          .setValue(context);

      return this;
    }

    public Builder setSetting(CodeDTO settingDTO) {
      CodeableConcept setting = toSnomedCode(settingDTO);

      addUniqueParameter(SystemConstants.SETTING)
          .setValue(setting);

      return this;
    }

    public Builder addInputData(Resource resource) {
      addParameter(SystemConstants.INPUT_DATA)
          .setResource(resource);

      return this;
    }

    public Builder addInputData(Reference reference) {
      addParameter(SystemConstants.INPUT_DATA)
          .setValue(reference);

      return this;
    }

    public ParametersParameterComponent getUnique(String name) {
      return Iterables.getOnlyElement(parameterNames.get(name));
    }
  }
}
