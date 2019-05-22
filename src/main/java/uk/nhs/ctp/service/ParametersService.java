package uk.nhs.ctp.service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

import org.hl7.fhir.dstu3.model.Attachment;
import org.hl7.fhir.dstu3.model.BooleanType;
import org.hl7.fhir.dstu3.model.CodeableConcept;
import org.hl7.fhir.dstu3.model.Coding;
import org.hl7.fhir.dstu3.model.ContactPoint;
import org.hl7.fhir.dstu3.model.DateTimeType;
import org.hl7.fhir.dstu3.model.DecimalType;
import org.hl7.fhir.dstu3.model.Enumerations;
import org.hl7.fhir.dstu3.model.HumanName;
import org.hl7.fhir.dstu3.model.IdType;
import org.hl7.fhir.dstu3.model.Immunization;
import org.hl7.fhir.dstu3.model.IntegerType;
import org.hl7.fhir.dstu3.model.MedicationAdministration;
import org.hl7.fhir.dstu3.model.MedicationAdministration.MedicationAdministrationStatus;
import org.hl7.fhir.dstu3.model.Meta;
import org.hl7.fhir.dstu3.model.Observation;
import org.hl7.fhir.dstu3.model.Parameters;
import org.hl7.fhir.dstu3.model.Parameters.ParametersParameterComponent;
import org.hl7.fhir.dstu3.model.Person;
import org.hl7.fhir.dstu3.model.QuestionnaireResponse;
import org.hl7.fhir.dstu3.model.QuestionnaireResponse.QuestionnaireResponseStatus;
import org.hl7.fhir.dstu3.model.Reference;
import org.hl7.fhir.dstu3.model.StringType;
import org.hl7.fhir.exceptions.FHIRException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import uk.nhs.ctp.SystemConstants;
import uk.nhs.ctp.SystemURL;
import uk.nhs.ctp.entities.CaseImmunization;
import uk.nhs.ctp.entities.CaseMedication;
import uk.nhs.ctp.entities.CaseObservation;
import uk.nhs.ctp.entities.CaseParameter;
import uk.nhs.ctp.entities.Cases;
import uk.nhs.ctp.exception.EMSException;
import uk.nhs.ctp.repos.CaseRepository;
import uk.nhs.ctp.service.builder.CareConnectPatientBuilder;
import uk.nhs.ctp.service.builder.RelatedPersonBuilder;
import uk.nhs.ctp.service.dto.SettingsDTO;
import uk.nhs.ctp.service.dto.TriageQuestion;
import uk.nhs.ctp.utils.ErrorHandlingUtils;
import uk.nhs.ctp.utils.ResourceProviderUtils;

@Service
public class ParametersService {

	private static final Logger LOG = LoggerFactory.getLogger(ParametersService.class);

	@Autowired
	private CaseRepository caseRepository;
	
	@Autowired
	private CareConnectPatientBuilder careConnectPatientBuilder;
	
	@Autowired
	private RelatedPersonBuilder relatedPersonBuilder;

	public Parameters getEvaluateParameters(Long caseId, TriageQuestion[] questionResponse, SettingsDTO settings,
			Boolean amending) {

		Cases caseEntity = caseRepository.findOne(caseId);
		ErrorHandlingUtils.checkEntityExists(caseEntity, "Case");

		Parameters parameters = new Parameters();
		parameters.setMeta(new Meta().addProfile(SystemURL.SERVICE_DEFINITION_EVALUATE));

		setRequestId(caseId, parameters);

		try {
			parameters.addParameter().setName(SystemConstants.PATIENT).setResource(careConnectPatientBuilder.build(caseEntity));
		} catch (FHIRException e) {
			LOG.error("Cannot parse gender code", e);
			throw new EMSException(HttpStatus.INTERNAL_SERVER_ERROR, "Cannot parse patient gender code", e);
		}

		setObservations(caseEntity, parameters);
		setContext(caseEntity, parameters);
		setQuestionnaireResponse(questionResponse, parameters, amending);
		addObservationInputData(caseEntity, parameters);
		addImmunizationInputData(caseEntity, parameters);
		addMedicationInputData(caseEntity, parameters);
		
		// Add extra parameters
		addParameterInputData(caseEntity, parameters);

		// set missing parameters
		// initiatingPerson, userType, userLanguage, userTaskContext, receivingPerson,
		// recipientType, recipientLanguage, setting
		try {
			setInitiatingPerson(caseEntity, parameters, settings);
		} catch (FHIRException e) {
			LOG.error("Cannot parse gender code", e);
			throw new EMSException(HttpStatus.INTERNAL_SERVER_ERROR, "Cannot parse person gender code", e);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		setUserType(caseEntity, parameters, settings);
		setUserLanguage(caseEntity, parameters, settings);
		setUserTaskContext(caseEntity, parameters, settings);
		try {
			setReceivingPerson(caseEntity, parameters, settings);
		} catch (FHIRException e) {
			LOG.error("Cannot parse gender code", e);
			throw new EMSException(HttpStatus.INTERNAL_SERVER_ERROR, "Cannot parse person gender code", e);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		setRecipientType(caseEntity, parameters, settings);
		setRecipientLanguage(caseEntity, parameters, settings);
		setSetting(caseEntity, parameters, settings);

		return parameters;
	}

	// Adjust to get actual Person.
	private void setInitiatingPerson(Cases caseEntity, Parameters parameters, SettingsDTO settings)
			throws FHIRException, ParseException {

		List<HumanName> names = new ArrayList<HumanName>();
		List<ContactPoint> telecom = new ArrayList<ContactPoint>();

		names.add(new HumanName().setFamily(settings.getInitiatingPerson().getName().split(" ")[1])
				.addGiven(settings.getInitiatingPerson().getName().split(" ")[0]));
		telecom.add(new ContactPoint().setValue(settings.getInitiatingPerson().getTelecom()));

		parameters.addParameter().setName(SystemConstants.INITIATINGPERSON).setResource(new Person().setName(names)
				.setTelecom(telecom)
				.setGender(Enumerations.AdministrativeGender.fromCode(settings.getInitiatingPerson().getGender()))
				.setBirthDate(new SimpleDateFormat("yyyy-MM-dd").parse(settings.getInitiatingPerson().getBirthDate())));
	}

	private void setUserType(Cases caseEntity, Parameters parameters, SettingsDTO settings) {

		if (caseEntity.getSkillset().getCode().equalsIgnoreCase("PA")) {
			parameters.addParameter().setName(SystemConstants.USERTYPE).setValue(new CodeableConcept().addCoding()
					.setCode("116154003").setDisplay("Patient").setSystem(SystemURL.SNOMED));
		} else {
			parameters.addParameter().setName(SystemConstants.USERTYPE)
					.setValue(new CodeableConcept().addCoding().setCode(settings.getUserType().getCode())
							.setDisplay(settings.getUserType().getDisplay()).setSystem(SystemURL.SNOMED));
		}

	}

	private void setUserLanguage(Cases caseEntity, Parameters parameters, SettingsDTO settings) {
		parameters.addParameter().setName(SystemConstants.USERLANGUAGE)
				.setValue(new CodeableConcept().addCoding().setCode(settings.getUserLanguage().getCode())
						.setDisplay(settings.getUserLanguage().getDisplay()).setSystem(SystemURL.DATA_DICTIONARY));

	}

	private void setUserTaskContext(Cases caseEntity, Parameters parameters, SettingsDTO settings) {
		parameters.addParameter().setName(SystemConstants.USERTASKCONTEXT)
				.setValue(new CodeableConcept().addCoding().setCode(settings.getUserTaskContext().getCode())
						.setDisplay(settings.getUserTaskContext().getDisplay()).setSystem(SystemURL.SNOMED));

	}

	private void setReceivingPerson(Cases caseEntity, Parameters parameters, SettingsDTO settings)
			throws FHIRException, ParseException {

		List<HumanName> names = new ArrayList<HumanName>();
		List<ContactPoint> telecom = new ArrayList<ContactPoint>();

		names.add(new HumanName().setFamily(settings.getReceivingPerson().getName().split(" ")[1])
				.addGiven(settings.getReceivingPerson().getName().split(" ")[0]));
		telecom.add(new ContactPoint().setValue(settings.getReceivingPerson().getTelecom()));

		parameters.addParameter().setName(SystemConstants.RECIEVINGPERSON).setResource(new Person().setName(names)
				.setTelecom(telecom)
				.setGender(Enumerations.AdministrativeGender.fromCode(settings.getReceivingPerson().getGender()))
				.setBirthDate(new SimpleDateFormat("yyyy-MM-dd").parse(settings.getReceivingPerson().getBirthDate())));

	}

	private void setRecipientType(Cases caseEntity, Parameters parameters, SettingsDTO settings) {
		parameters.addParameter().setName(SystemConstants.RECIPIENTTYPE)
				.setValue(new CodeableConcept().addCoding().setCode(settings.getRecipientType().getCode())
						.setDisplay(settings.getRecipientType().getDisplay()).setSystem(SystemURL.SNOMED));

	}

	private void setRecipientLanguage(Cases caseEntity, Parameters parameters, SettingsDTO settings) {
		parameters.addParameter().setName(SystemConstants.RECIPIENTLANGUAGE)
				.setValue(new CodeableConcept().addCoding().setCode(settings.getRecipientLanguage().getCode())
						.setDisplay(settings.getRecipientLanguage().getDisplay()).setSystem(SystemURL.DATA_DICTIONARY));

	}

	private void setSetting(Cases caseEntity, Parameters parameters, SettingsDTO settings) {
		parameters.addParameter().setName(SystemConstants.SETTING)
				.setValue(new CodeableConcept().addCoding().setCode(settings.getSetting().getCode())
						.setDisplay(settings.getSetting().getDisplay()).setSystem(SystemURL.SNOMED));

	}

	private void addImmunizationInputData(Cases caseEntity, Parameters parameters) {
		if (!caseEntity.getImmunizations().isEmpty()) {
			for (CaseImmunization immunizationEntity : caseEntity.getImmunizations()) {
				Immunization immunization = new Immunization().setStatus(Immunization.ImmunizationStatus.COMPLETED)
						.setVaccineCode(new CodeableConcept().addCoding(new Coding(SystemURL.SNOMED,
								immunizationEntity.getCode(), immunizationEntity.getDisplay())))
						.setNotGiven(immunizationEntity.getNotGiven());
				parameters.addParameter().setName(SystemConstants.INPUT_DATA).setResource(immunization);

			}
		}
	}

	private void addMedicationInputData(Cases caseEntity, Parameters parameters) {
		if (!caseEntity.getMedications().isEmpty()) {
			for (CaseMedication medicationEntity : caseEntity.getMedications()) {
				MedicationAdministration medication = new MedicationAdministration()
						.setStatus(MedicationAdministrationStatus.COMPLETED)
						.setMedication(new CodeableConcept().addCoding(new Coding(SystemURL.SNOMED,
								medicationEntity.getCode(), medicationEntity.getDisplay())))
						.setNotGiven(medicationEntity.getNotGiven());
				parameters.addParameter().setName(SystemConstants.INPUT_DATA).setResource(medication);

			}
		}
	}

	private void addObservationInputData(Cases caseEntity, Parameters parameters) {
		if (!caseEntity.getObservations().isEmpty()) {
			for (CaseObservation observationEntity : caseEntity.getObservations()) {
				Observation observation = new Observation().setStatus(Observation.ObservationStatus.FINAL)
						.setCode(new CodeableConcept().addCoding(new Coding(SystemURL.SNOMED,
								observationEntity.getCode(), observationEntity.getDisplay())))
						.setValue(new BooleanType(observationEntity.getValue()));

				if (observationEntity.getDataAbsentCode() != null && observationEntity.getDataAbsentDisplay() != null) {
					observation.setDataAbsentReason(new CodeableConcept().addCoding(new Coding(SystemURL.SNOMED,
							observationEntity.getDataAbsentCode(), observationEntity.getDataAbsentDisplay())));
				}

				parameters.addParameter().setName(SystemConstants.INPUT_DATA).setResource(observation);

			}
		}
	}
	
	private void addParameterInputData(Cases caseEntity, Parameters parameters) {
		if(!caseEntity.getParameters().isEmpty()) {
			for (CaseParameter parameterEntity : caseEntity.getParameters()) {
				ParametersParameterComponent parameter = new ParametersParameterComponent();
				parameter.setName(parameterEntity.getName());
				parameter.setValue(new StringType(parameterEntity.getValue()));
				parameters.addParameter(parameter);
			}
		}
	}

	private void setQuestionnaireResponse(TriageQuestion[] questionResponse, Parameters parameters, Boolean amending) {
		if (questionResponse != null) {

			QuestionnaireResponse questionnaireResponse = new QuestionnaireResponse().setQuestionnaire(
					new Reference(new IdType(SystemConstants.QUESTIONNAIRE, questionResponse[0].getQuestionnaireId().replace("#", ""))));
			if (amending) {
				questionnaireResponse.setStatus(QuestionnaireResponseStatus.AMENDED);
			} else {
				questionnaireResponse.setStatus(QuestionnaireResponseStatus.COMPLETED);
			}

			for (TriageQuestion triageQuestion : questionResponse) {
				if (triageQuestion.getQuestionType().equalsIgnoreCase("STRING")) {
					questionnaireResponse.addItem().setLinkId(triageQuestion.getQuestionId())
							.setText(triageQuestion.getQuestion()).addAnswer()
							.setValue(new StringType(triageQuestion.getResponseString()));
				} // check for new types
				else if (triageQuestion.getQuestionType().equalsIgnoreCase("INTEGER")) {
					questionnaireResponse.addItem().setLinkId(triageQuestion.getQuestionId())
							.setText(triageQuestion.getQuestion()).addAnswer()
							.setValue(new IntegerType(triageQuestion.getResponseInterger()));
				} else if (triageQuestion.getQuestionType().equalsIgnoreCase("BOOLEAN")) {
					questionnaireResponse.addItem().setLinkId(triageQuestion.getQuestionId())
							.setText(triageQuestion.getQuestion()).addAnswer()
							.setValue(new BooleanType(triageQuestion.getResponceBoolean()));
				} else if (triageQuestion.getQuestionType().equalsIgnoreCase("DECIMAL")) {
					questionnaireResponse.addItem().setLinkId(triageQuestion.getQuestionId())
							.setText(triageQuestion.getQuestion()).addAnswer()
							.setValue(new DecimalType(triageQuestion.getResponseDecimal()));
				} else if (triageQuestion.getQuestionType().equalsIgnoreCase("DATE")) {
					questionnaireResponse.addItem().setLinkId(triageQuestion.getQuestionId())
							.setText(triageQuestion.getQuestion()).addAnswer()
							.setValue(new DateTimeType(triageQuestion.getResponseDate()));
				} else if (triageQuestion.getQuestionType().equalsIgnoreCase("ATTACHMENT")) {
					Attachment attachment = new Attachment();
					attachment.setContentType(triageQuestion.getResponseAttachmentType());
					attachment.setData(Base64.getEncoder().encode(triageQuestion.getResponseAttachment().getBytes()));

					questionnaireResponse.addItem().setLinkId(triageQuestion.getQuestionId())
							.setText(triageQuestion.getQuestion()).addAnswer().setValue(attachment);
				} else {
					questionnaireResponse.addItem().setLinkId(triageQuestion.getQuestionId())
							.setText(triageQuestion.getQuestion()).addAnswer()
							.setValue(new Coding().setCode(triageQuestion.getResponse().getCode())
									.setDisplay(triageQuestion.getResponse().getDisplay()));
				}
			}
			
			ParametersParameterComponent partyComponent = 
					ResourceProviderUtils.getParameterByName(
						ResourceProviderUtils.getParameterByName(
							ResourceProviderUtils.getParameterAsResource(
									parameters.getParameter(), SystemConstants.INPUT_PARAMETERS, Parameters.class)
							.getParameter(), SystemConstants.CONTEXT)
						.getPart(), SystemConstants.PARTY);
					
			questionnaireResponse.setSource(new Reference(partyComponent.getValue().primitiveValue().equals("1") ?
					ResourceProviderUtils.getParameterAsResource(parameters.getParameter(), SystemConstants.PATIENT) :
					relatedPersonBuilder.build()));

			parameters.addParameter().setName(SystemConstants.INPUT_DATA).setResource(questionnaireResponse);
		}
	}

	private void setContext(Cases caseEntity, Parameters parameters) {
		ParametersParameterComponent inputParameters = parameters.addParameter()
				.setName(SystemConstants.INPUT_PARAMETERS);
		Parameters inputParamsResource = new Parameters();

		inputParamsResource.addParameter().setName(SystemConstants.CONTEXT)
				.addPart(new ParametersParameterComponent().setName(SystemConstants.PARTY)
						.setValue(new StringType(caseEntity.getParty().getCode())))
				.addPart(new ParametersParameterComponent().setName(SystemConstants.SKILLSET)
						.setValue(new StringType(caseEntity.getSkillset().getCode())));

		inputParameters.setResource(inputParamsResource);
	}

	private void setObservations(Cases caseEntity, Parameters parameters) throws FHIRException {

		Observation genderObservation = new Observation().setStatus(Observation.ObservationStatus.FINAL)
				.setCode(new CodeableConcept().addCoding(new Coding(SystemURL.SNOMED, "263495000", "Gender")))
				.setDataAbsentReason(new CodeableConcept().addCoding(new Coding(SystemURL.SNOMED, null, null)))
				.setValue(new StringType(caseEntity.getGender()));
		parameters.addParameter().setName(SystemConstants.INPUT_DATA).setResource(genderObservation);

		Observation ageObservation = new Observation().setStatus(Observation.ObservationStatus.FINAL)
				.setCode(new CodeableConcept().addCoding(new Coding(SystemURL.SNOMED, "397669002", "Age")))
				.setDataAbsentReason(new CodeableConcept().addCoding(new Coding(SystemURL.SNOMED, null, null)))
				.setValue(new StringType(caseEntity.getDateOfBirth().toString()));
		parameters.addParameter().setName(SystemConstants.INPUT_DATA).setResource(ageObservation);
	}

	private void setRequestId(Long caseId, Parameters parameters) {
		parameters.addParameter().setName(SystemConstants.REQUEST_ID).setValue(new StringType(String.valueOf(caseId)));
	}
}
