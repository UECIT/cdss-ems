package uk.nhs.ctp.service.report.decorator.mapping.template;

import javax.xml.bind.JAXBElement;
import javax.xml.namespace.QName;

import org.hl7.fhir.dstu3.model.Location;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import uk.nhs.ctp.service.dto.ReportRequestDTO;
import uk.nhs.ctp.service.report.decorator.mapping.LocationToCOCDTP145222GB02PlaceMapper;
import uk.nhs.ctp.service.report.org.hl7.v3.COCDTP145222GB02HealthCareFacility;
import uk.nhs.ctp.service.report.org.hl7.v3.COCDTP145222GB02HealthCareFacility.TemplateId;
import uk.nhs.ctp.service.report.org.hl7.v3.COCDTP145222GB02Place;
import uk.nhs.ctp.service.report.org.hl7.v3.COCDTP146232GB01Location;

@Component
public class LocationToIncidentalHealthCareFacilityUniversalTemplateMapper 
		implements TemplateMapper<Location, COCDTP146232GB01Location> {

	@Autowired
	private LocationToCOCDTP145222GB02PlaceMapper locationToPlaceMapper;
	
	@Override
	public void map(Location location, COCDTP146232GB01Location container, ReportRequestDTO request) {
		COCDTP145222GB02HealthCareFacility healthCareFacility = new COCDTP145222GB02HealthCareFacility();
		healthCareFacility.setClassCode(healthCareFacility.getClassCode());
		
		TemplateId templateId = new TemplateId();
		templateId.setRoot("2.16.840.1.113883.2.1.3.2.4.18.2");
		templateId.setExtension(getTemplateName());
		healthCareFacility.setTemplateId(templateId);
		
		healthCareFacility.setLocation(new JAXBElement<COCDTP145222GB02Place>(
				new QName("location"), COCDTP145222GB02Place.class, locationToPlaceMapper.map(location)));
		
		container.setCOCDTP145222GB02HealthCareFacility(healthCareFacility);
	}

	@Override
	public Class<Location> getResourceClass() {
		return Location.class;
	}

	@Override
	public String getTemplateName() {
		return "COCD_TP145222GB02#HealthCareFacility";
	}

}
