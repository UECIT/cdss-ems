package uk.nhs.ctp.service.report.decorator.mapping.template.author;

import org.hl7.fhir.dstu3.model.CareConnectOrganization;
import org.hl7.fhir.dstu3.model.CareConnectRelatedPerson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import uk.nhs.ctp.service.dto.ReportRequestDTO;
import uk.nhs.ctp.service.report.decorator.mapping.AddressToADMapper;
import uk.nhs.ctp.service.report.decorator.mapping.CodingToCVNPfITCodedplainRequiredMapper;
import uk.nhs.ctp.service.report.decorator.mapping.HumanNameToCOCDTP145200GB01PersonMapper;
import uk.nhs.ctp.service.report.decorator.mapping.OrganizationToCOCDTP145200GB01OrganizationMapper;
import uk.nhs.ctp.service.report.decorator.mapping.template.TemplateMapper;
import uk.nhs.ctp.service.report.org.hl7.v3.COCDTP145200GB01AssignedAuthor;
import uk.nhs.ctp.service.report.org.hl7.v3.COCDTP145200GB01AssignedAuthor.TemplateId;
import uk.nhs.ctp.service.report.org.hl7.v3.IINPfITOidRequiredAssigningAuthorityName;
import uk.nhs.ctp.service.report.org.hl7.v3.POCDMT200001GB02Author;

@Component
public class RelatedPersonToAuthorPersonUniversalTemplateMapper implements TemplateMapper<CareConnectRelatedPerson, POCDMT200001GB02Author> {

	@Autowired
	private AddressToADMapper addressToADMapper;
	
	@Autowired
	private CodingToCVNPfITCodedplainRequiredMapper codingMapper;
	
	@Autowired
	private OrganizationToCOCDTP145200GB01OrganizationMapper organizationToOrganizationMapper;
	
	@Autowired
	private HumanNameToCOCDTP145200GB01PersonMapper humanNameToAssignedPersonMapper;
	
	@Override
	public void map(CareConnectRelatedPerson relatedPerson, POCDMT200001GB02Author author, ReportRequestDTO request) {
		var organization = (CareConnectOrganization)
				request.getReferralRequest().getRequester().getOnBehalfOf().getResource();
		
		COCDTP145200GB01AssignedAuthor assignedAuthor = new COCDTP145200GB01AssignedAuthor();
		assignedAuthor.setClassCode(assignedAuthor.getClassCode());
		assignedAuthor.getAddr().add(addressToADMapper.map(relatedPerson.getAddressFirstRep()));
		assignedAuthor.setCode(codingMapper.map(relatedPerson.getRelationship().getCodingFirstRep()));
		
		assignedAuthor.setAssignedPerson(humanNameToAssignedPersonMapper.map(relatedPerson.getNameFirstRep()));
		assignedAuthor.setRepresentedOrganization(organizationToOrganizationMapper.map(organization));
		
		TemplateId assignedAuthorTemplate = new TemplateId();
		assignedAuthorTemplate.setRoot("2.16.840.1.113883.2.1.3.2.4.18.2");
		assignedAuthorTemplate.setExtension(getTemplateName());
		assignedAuthor.setTemplateId(assignedAuthorTemplate);
				
		IINPfITOidRequiredAssigningAuthorityName id = new IINPfITOidRequiredAssigningAuthorityName();
		id.setRoot("1.2.826.0.1285.0.2.0.65");
		id.setExtension("24400320");
		assignedAuthor.getId().add(id);
		
		author.setCOCDTP145200GB01AssignedAuthor(assignedAuthor);
	}

	@Override
	public Class<CareConnectRelatedPerson> getResourceClass() {
		return CareConnectRelatedPerson.class;
	}

	@Override
	public String getTemplateName() {
		return "COCD_TP145200GB01#AssignedAuthor";
	}

}
