package uk.nhs.ctp.service.report.decorator.mapping;

import org.springframework.stereotype.Component;

import uk.nhs.ctp.service.report.org.hl7.v3.COCDTP145200GB01Organization;
import uk.nhs.ctp.service.report.org.hl7.v3.COCDTP145200GB01Organization.Id;
import uk.nhs.ctp.service.report.org.hl7.v3.COCDTP145200GB01Organization.TemplateId;

@Component
public class OrganizationToCOCDTP145200GB01OrganizationMapper extends OrganizationToOrganizationMapper<COCDTP145200GB01Organization> {

	@Override
	protected COCDTP145200GB01Organization createOrganization() {
		return new COCDTP145200GB01Organization();
	}

	@Override
	protected void addIds(COCDTP145200GB01Organization organization) {
		Id id = new Id();
    	id.setRoot("2.16.840.1.113883.2.1.3.2.4.19.2");
    	id.setExtension("K83032");
		organization.setId(id);
		
		TemplateId templateId = new TemplateId();
		templateId.setRoot("2.16.840.1.113883.2.1.3.2.4.18.2");
		templateId.setExtension("COCD_TP145200GB01#representedOrganization");
		
		organization.setTemplateId(templateId);
	}

}
