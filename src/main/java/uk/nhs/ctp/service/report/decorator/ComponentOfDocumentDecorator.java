package uk.nhs.ctp.service.report.decorator;

import javax.xml.bind.JAXBElement;
import javax.xml.namespace.QName;

import org.hl7.fhir.dstu3.model.Bundle;
import org.hl7.fhir.dstu3.model.Composition;
import org.hl7.fhir.dstu3.model.Encounter;
import org.hl7.fhir.dstu3.model.Location;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import uk.nhs.ctp.service.dto.ReportRequestDTO;
import uk.nhs.ctp.service.report.decorator.mapping.template.resolver.EncounterParticipantTemplateResolver;
import uk.nhs.ctp.service.report.decorator.mapping.template.resolver.HealthCareFacilityChoiceTemplateResolver;
import uk.nhs.ctp.service.report.decorator.mapping.template.resolver.ResponsiblePartyChoiceTemplateResolver;
import uk.nhs.ctp.service.report.npfit.hl7.localisation.TemplateContent;
import uk.nhs.ctp.service.report.org.hl7.v3.COCDTP146232GB01EncompassingEncounter;
import uk.nhs.ctp.service.report.org.hl7.v3.COCDTP146232GB01EncounterParticipant;
import uk.nhs.ctp.service.report.org.hl7.v3.COCDTP146232GB01Location;
import uk.nhs.ctp.service.report.org.hl7.v3.COCDTP146232GB01ResponsibleParty;
import uk.nhs.ctp.service.report.org.hl7.v3.CV;
import uk.nhs.ctp.service.report.org.hl7.v3.POCDMT200001GB02ClinicalDocument;
import uk.nhs.ctp.service.report.org.hl7.v3.POCDMT200001GB02Component;
import uk.nhs.ctp.utils.ResourceProviderUtils;

@Component
public class ComponentOfDocumentDecorator implements OneOneOneDecorator {

	@Autowired
	private EncounterParticipantTemplateResolver<? extends IBaseResource> encounterParticipantTemplateResolver;
	
	@Autowired
	private HealthCareFacilityChoiceTemplateResolver<? extends IBaseResource> healthCareFacilityChoiceTemplateResolver;
	
	@Autowired
	private ResponsiblePartyChoiceTemplateResolver<? extends IBaseResource> responsiblePartyChoiceTemplateResolver;
	
	@Override
	public void decorate(POCDMT200001GB02ClinicalDocument document, ReportRequestDTO request) {
		POCDMT200001GB02Component componentOf = new POCDMT200001GB02Component();
		componentOf.setTypeCode(componentOf.getTypeCode());
		
		TemplateContent templateContent = new TemplateContent();
		templateContent.setRoot("2.16.840.1.113883.2.1.3.2.4.18.16");
		templateContent.setExtension("COCD_TP146232GB01#EncompassingEncounter");
		componentOf.setContentId(templateContent);
		
		COCDTP146232GB01EncompassingEncounter encompassingEncounter = new COCDTP146232GB01EncompassingEncounter();
		encompassingEncounter.setClassCode(encompassingEncounter.getClassCode());
		encompassingEncounter.setMoodCode(encompassingEncounter.getMoodCode());
		encompassingEncounter.setDischargeDispositionCode(null); //where does this come from??????
		
		CV cv = new CV();
		cv.setCodeSystem("2.16.840.1.113883.2.1.3.2.4.17.326");
		cv.setCode("NHS111Encounter");
		encompassingEncounter.setCode(cv);
		
		Bundle resourceBundle = ResourceProviderUtils.getResource(
				request.getReferralRequest().getContained(), Bundle.class);
		Encounter encounter = ResourceProviderUtils.getResource(ResourceProviderUtils.getResource(
				resourceBundle, Composition.class).getEncounter().getResource(), Encounter.class);
		
		COCDTP146232GB01EncounterParticipant encounterParticipant = 
				encounterParticipantTemplateResolver.resolve(encounter.getSubject().getResource(), request);
		
		if (encounterParticipant != null)
			encompassingEncounter.getEncounterParticipant().add(encounterParticipant);
		
		encounter.getParticipant().stream()
				.map(component -> component.getIndividual().getResource())
				.forEach(resource -> {
					COCDTP146232GB01EncounterParticipant individualParticipant = 
							encounterParticipantTemplateResolver.resolve(resource, request);
					
					if (individualParticipant != null)
						encompassingEncounter.getEncounterParticipant().add(individualParticipant);
				});

		Location fhirLocation = ResourceProviderUtils.getResource(
				encounter.getLocationFirstRep().getLocation().getResource(), Location.class);
		
		COCDTP146232GB01Location location = healthCareFacilityChoiceTemplateResolver.resolve(fhirLocation, request);
		encompassingEncounter.setLocation(location);
		
		COCDTP146232GB01ResponsibleParty responsibleParty = responsiblePartyChoiceTemplateResolver.resolve(
				request.getReferralRequest().getRequester().getAgent().getResource(), request);
		
		encompassingEncounter.setResponsibleParty(new JAXBElement<COCDTP146232GB01ResponsibleParty>(
				new QName("responsibleParty"), COCDTP146232GB01ResponsibleParty.class, responsibleParty));
		
		componentOf.setCOCDTP146232GB01EncompassingEncounter(encompassingEncounter);
		
		document.setComponentOf(componentOf);
	}

}
