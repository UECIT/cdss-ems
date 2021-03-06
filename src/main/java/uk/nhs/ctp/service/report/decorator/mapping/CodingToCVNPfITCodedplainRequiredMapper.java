package uk.nhs.ctp.service.report.decorator.mapping;

import java.util.HashMap;
import java.util.Map;

import org.hl7.fhir.dstu3.model.Coding;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import uk.nhs.ctp.service.TerminologyService;
import uk.nhs.ctp.service.report.org.hl7.v3.CVNPfITCodedplainRequired;

@Component
public class CodingToCVNPfITCodedplainRequiredMapper extends AbstractMapper<CVNPfITCodedplainRequired, Coding> {

	@Autowired
	private TerminologyService terminologyService;
	
	// to be substituted with calls to the terminology server whenever possible
	private Map<String, CVNPfITCodedplainRequired> codeMap = new HashMap<>();
	
	private Map<String, String> systemMap = new HashMap<>();;
	
	public CodingToCVNPfITCodedplainRequiredMapper() {
		CVNPfITCodedplainRequired callOperatorCode = new CVNPfITCodedplainRequired();
		callOperatorCode.setCode("NR1680");
		callOperatorCode.setCodeSystem("2.16.840.1.113883.2.1.3.2.4.17.124");
		callOperatorCode.setDisplayName("Call Operator");
		
		codeMap.put("https://fhir.hl7.org.uk/STU3/CodeSystem/CareConnect-SDSJobRoleName-1R1690", callOperatorCode);
			
		systemMap.put("http://uecdi-tom-terminology.eu-west-2.elasticbeanstalk.com/fhir/ValueSet/sex", "2.16.840.1.113883.2.1.3.2.4.16.25");
		systemMap.put("http://uecdi-tom-terminology.eu-west-2.elasticbeanstalk.com/fhir/ValueSet/human-language", "2.16.840.1.113883.2.1.3.2.4.17.70");
	}
	
	@Override
	public CVNPfITCodedplainRequired map(Coding coding) {
		return codeMap.get(coding.getSystem() + coding.getCode());
	}
	
	public CVNPfITCodedplainRequired map(Coding coding, String targetSystem) {
		return map(coding.getSystem(), targetSystem, coding.getCode());
	}
	
	public CVNPfITCodedplainRequired map(String sourceSystem, String targetSystem, String code) {
		CVNPfITCodedplainRequired newCode = terminologyService.getCode(sourceSystem, targetSystem, code);
		newCode.setCodeSystem(systemMap.containsKey(targetSystem) ? systemMap.get(targetSystem): targetSystem);
		return newCode;
	}
}
