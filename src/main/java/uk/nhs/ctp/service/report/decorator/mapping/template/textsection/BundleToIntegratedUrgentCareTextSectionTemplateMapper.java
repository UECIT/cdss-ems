package uk.nhs.ctp.service.report.decorator.mapping.template.textsection;

import java.util.List;
import java.util.stream.Collectors;

import javax.xml.bind.JAXBElement;
import javax.xml.namespace.QName;

import org.hl7.fhir.dstu3.model.Bundle;
import org.hl7.fhir.dstu3.model.CareConnectCarePlan;
import org.hl7.fhir.dstu3.model.Coding;
import org.hl7.fhir.dstu3.model.QuestionnaireResponse;
import org.hl7.fhir.dstu3.model.Type;
import org.springframework.stereotype.Component;

import uk.nhs.ctp.service.report.org.hl7.v3.COCDTP146246GB01Section1;
import uk.nhs.ctp.service.report.org.hl7.v3.StrucDocBr;
import uk.nhs.ctp.service.report.org.hl7.v3.StrucDocParagraph;
import uk.nhs.ctp.service.report.org.hl7.v3.StrucDocText;
import uk.nhs.ctp.utils.ResourceProviderUtils;

@Component
public class BundleToIntegratedUrgentCareTextSectionTemplateMapper extends AbstractIntegratedUrgentCareTextSectionTemplateMapper<Bundle> {
	
	@Override
	public void map(Bundle bundle, COCDTP146246GB01Section1 section) {
		StrucDocText text = new StrucDocText();
		
		List<QuestionnaireResponse> questionnaireResponses = 
				ResourceProviderUtils.getResources(bundle, QuestionnaireResponse.class);

		List<CareConnectCarePlan> carePlans = 
				ResourceProviderUtils.getResources(bundle, CareConnectCarePlan.class);
		
		StrucDocParagraph questionnaireHeading = new StrucDocParagraph();
		questionnaireHeading.getContent().add("Information");
		text.getContent().add(new JAXBElement<StrucDocParagraph>(
				new QName("urn:hl7-org:v3", "paragraph"), StrucDocParagraph.class, questionnaireHeading));
		
		questionnaireResponses.stream().forEach(response ->
			addQuestionnaireResponse(text, response));
		
		StrucDocParagraph carePlanHeading = new StrucDocParagraph();
		carePlanHeading.getContent().add("Care Advice");
		text.getContent().add(new JAXBElement<StrucDocParagraph>(
				new QName("urn:hl7-org:v3", "paragraph"), StrucDocParagraph.class, carePlanHeading));
		
		carePlans.stream().forEach(plan -> addCarePlan(text, plan));

		section.setTitle("Triage Notes");
		section.setText(text);
	}
	
	@Override
	public Class<Bundle> getResourceClass() {
		return Bundle.class;
	}

	private void addQuestionnaireResponse(StrucDocText text, QuestionnaireResponse response) {
		response.getItem().stream().forEach(item -> {
			StrucDocParagraph paragraph = new StrucDocParagraph();
			
			paragraph.getContent().add(item.getText());
			JAXBElement<StrucDocBr> br = new JAXBElement<>(new QName("urn:hl7-org:v3", "br"), StrucDocBr.class, new StrucDocBr());
			paragraph.getContent().add(br);
			Type type = item.getAnswerFirstRep().getValue();

			paragraph.getContent().add(type instanceof Coding ? ((Coding)type).getDisplay() : type.primitiveValue());
		
			text.getContent().add(new JAXBElement<StrucDocParagraph>(
					new QName("urn:hl7-org:v3", "paragraph"), StrucDocParagraph.class, paragraph));
		});
	}
	
	private void addCarePlan(StrucDocText text, CareConnectCarePlan carePlan) {
		StrucDocParagraph paragraph = new StrucDocParagraph();
		
		paragraph.getContent().addAll(carePlan.getActivity().stream().map(activity -> 
				activity.getDetail().getDescription()).collect(Collectors.toList()));
		
		paragraph.getContent().addAll(carePlan.getNote().stream().map(note -> 
				note.getText()).collect(Collectors.toList()));
		
		text.getContent().add(new JAXBElement<StrucDocParagraph>(
				new QName("urn:hl7-org:v3", "paragraph"), StrucDocParagraph.class, paragraph));
	}
}
