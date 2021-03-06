package uk.nhs.ctp.service.report.decorator.mapping.template.encompassingencounter.participant;

import java.util.List;

import org.hl7.fhir.dstu3.model.Address;
import org.hl7.fhir.dstu3.model.CareConnectOrganization;
import org.hl7.fhir.dstu3.model.CareConnectPractitioner;
import org.hl7.fhir.dstu3.model.ContactPoint;
import org.hl7.fhir.dstu3.model.HumanName;
import org.springframework.stereotype.Component;

import uk.nhs.ctp.service.report.org.hl7.v3.COCDTP146232GB01EncounterParticipant;

@Component
public class PractitionerToPersonWithOrganizationUniversalTemplateMapper 
		extends AbstractPersonWithOrganizationUniversalTemplateMapper<CareConnectPractitioner, COCDTP146232GB01EncounterParticipant> {
	
	@Override
	public Class<CareConnectPractitioner> getResourceClass() {
		return CareConnectPractitioner.class;
	}
	
	@Override
	public String getTemplateName() {
		return "COCD_TP145210GB01#AssignedEntity";
	}

	@Override
	protected HumanName getName(CareConnectPractitioner practitioner) {
		return practitioner.getNameFirstRep();
	}

	@Override
	protected List<ContactPoint> getTelecom(CareConnectPractitioner practitioner) {
		return practitioner.getTelecom();
	}

	@Override
	protected CareConnectOrganization getOrganization(CareConnectPractitioner practitioner) {
		CareConnectOrganization organization = new CareConnectOrganization();
		organization.setAddress(practitioner.getAddress());
		organization.setName(practitioner.getNameFirstRep().getNameAsSingleString());
		
		return organization;
	}

	@Override
	protected Address getAddress(CareConnectPractitioner practitioner) {
		return practitioner.getAddressFirstRep();
	}
}
