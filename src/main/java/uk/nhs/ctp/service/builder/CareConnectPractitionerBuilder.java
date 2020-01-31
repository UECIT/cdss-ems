package uk.nhs.ctp.service.builder;

import lombok.AllArgsConstructor;
import org.hl7.fhir.dstu3.model.Address;
import org.hl7.fhir.dstu3.model.Address.AddressType;
import org.hl7.fhir.dstu3.model.Address.AddressUse;
import org.hl7.fhir.dstu3.model.BooleanType;
import org.hl7.fhir.dstu3.model.CareConnectIdentifier;
import org.hl7.fhir.dstu3.model.CareConnectOrganization;
import org.hl7.fhir.dstu3.model.CareConnectPractitioner;
import org.hl7.fhir.dstu3.model.CodeableConcept;
import org.hl7.fhir.dstu3.model.Coding;
import org.hl7.fhir.dstu3.model.ContactPoint.ContactPointSystem;
import org.hl7.fhir.dstu3.model.ContactPoint.ContactPointUse;
import org.hl7.fhir.dstu3.model.HumanName;
import org.hl7.fhir.dstu3.model.Practitioner.PractitionerQualificationComponent;
import org.hl7.fhir.dstu3.model.Reference;
import org.hl7.fhir.dstu3.model.ResourceType;
import org.springframework.stereotype.Component;
import uk.nhs.ctp.SystemURL;
import uk.nhs.ctp.service.ReferenceService;

@Component
@AllArgsConstructor
public class CareConnectPractitionerBuilder {

  private final ReferenceService referenceService;

  public CareConnectPractitioner build(CareConnectOrganization assigningOrganization) {
    // TODO construct an appropriate record from the patient information
    CareConnectIdentifier sdsIdentifier = new CareConnectIdentifier();
    sdsIdentifier.setSystem("https://fhir.nhs.uk/Id/sds-user-id");
    sdsIdentifier.setAssigner(new Reference(assigningOrganization.getId()));
    sdsIdentifier.setValue("1");
    sdsIdentifier
        .setType(new CodeableConcept().addCoding(new Coding()
            .setSystem("https://fhir.nhs.uk/Id/sds-user-id")
            .setDisplay("SDS")
            .setCode("SDS")));

    CareConnectIdentifier sdsRoleIdentifier = new CareConnectIdentifier();
    sdsRoleIdentifier.setSystem("https://fhir.nhs.uk/Id/sds-role-profile-id");
    sdsIdentifier.setAssigner(new Reference(assigningOrganization.getId()));
    sdsRoleIdentifier.setValue("2");
    sdsRoleIdentifier.setType(new CodeableConcept().addCoding(new Coding()
        .setSystem("https://fhir.nhs.uk/Id/sds-role-profile-id")
        .setDisplay("SDS role")
        .setCode("SDSR")));

    CareConnectPractitioner.NhsCommunicationExtension communicationExtension = new CareConnectPractitioner.NhsCommunicationExtension();
    communicationExtension.setPreferred(new BooleanType(true));
    communicationExtension.setInterpreterRequired(new BooleanType(false));
    communicationExtension.setLanguage(CareConnectLanguage.bs.toCodeableConcept());
    communicationExtension.setModeOfCommunication(new CodeableConcept().addCoding(new Coding()
        .setSystem("https://fhir.hl7.org.uk/STU3/ValueSet/CareConnect-LanguageAbilityMode-1")
        .setDisplay("Received spoken")
        .setCode("RSP")));
    communicationExtension.setCommunicationProficiency(new CodeableConcept().addCoding(new Coding()
        .setSystem("https://fhir.hl7.org.uk/STU3/ValueSet/CareConnect-LanguageAbilityProficiency-1")
        .setDisplay("Excellent")
        .setCode("E")));

    Address gpAddress = new Address()
        .addLine("Dunelm Medical Practice")
        .addLine("Gilesgate Medical Centre")
        .addLine("Sunderland Rd")
        .setCity("Durham")
        .setPostalCode("S25 4HE")
        .setType(AddressType.PHYSICAL)
        .setUse(AddressUse.WORK);

    PractitionerQualificationComponent practitionerQualificationComponent = new PractitionerQualificationComponent();
    practitionerQualificationComponent.setCode(new CodeableConcept().addCoding(new Coding()
        .setCode("62247001")
        .setDisplay("General practitioner")
        .setSystem(SystemURL.SNOMED)));

    HumanName gpName = new HumanName().addPrefix("Dr").addGiven("M").setFamily("Khan");

    CareConnectPractitioner practitioner = new CareConnectPractitioner();
    practitioner.setId(referenceService.buildId(ResourceType.Practitioner, 1));
    practitioner.addName(gpName);
    practitioner.addAddress(gpAddress);
    practitioner.addIdentifier(sdsIdentifier);
    practitioner.addIdentifier(sdsRoleIdentifier);
    practitioner.setNhsCommunication(communicationExtension);
    practitioner.addQualification(practitionerQualificationComponent);
    practitioner.addTelecom().setValue("01914564521")
        .setSystem(ContactPointSystem.PHONE).setUse(ContactPointUse.WORK);

    return practitioner;

  }
}
