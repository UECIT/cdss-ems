package uk.nhs.ctp.service.report.decorator;

import java.math.BigInteger;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.mifmif.common.regex.Generex;

import uk.nhs.ctp.service.dto.ReportRequestDTO;
import uk.nhs.ctp.service.report.npfit.hl7.localisation.MessageType;
import uk.nhs.ctp.service.report.org.hl7.v3.CV;
import uk.nhs.ctp.service.report.org.hl7.v3.IINPfITUuidMandatory;
import uk.nhs.ctp.service.report.org.hl7.v3.POCDMT200001GB02ClinicalDocument;
import uk.nhs.ctp.service.report.org.hl7.v3.POCDMT200001GB02ClinicalDocument.EffectiveTime;
import uk.nhs.ctp.service.report.org.hl7.v3.POCDMT200001GB02ClinicalDocument.VersionNumber;
import uk.nhs.ctp.service.report.org.hl7.v3.POCDMT200001GB02ClinicalDocumentTypeId;

@Component
public class HeaderDataDocumentDecorator implements OneOneOneDecorator {

	@Autowired
	private Generex uuidGenerator;
	
	@Override
	public void decorate(POCDMT200001GB02ClinicalDocument document, ReportRequestDTO request) {

		document.setClassCode(document.getClassCode());
		document.setMoodCode(document.getMoodCode());

		// The HL7 attribute code uses a code from any vocabulary to describe the type of CDA document. 
		CV code = new CV();
		code.setCode("1066271000000101");
		code.setCodeSystem("2.16.840.1.113883.2.1.3.2.4.15");
		document.setCode(code);

		// The HL7 attribute confidentialitycode uses a code from any vocabulary to describe the confidentiality of the CDA document. T
		CV confidentialityCode = new CV();
		confidentialityCode.setCode("V");
		confidentialityCode.setCodeSystem("2.16.840.1.113883.1.11.16926");
		confidentialityCode.setDisplayName("very restricted");
		document.setConfidentialityCode(confidentialityCode);

		// The HL7 attribute effectiveTime is used to define the creation time of the CDA document.
		EffectiveTime effectiveTime = new EffectiveTime();
		SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmss");
		effectiveTime.setValue(format.format(new Date()));
		document.setEffectiveTime(effectiveTime);

		// The HL7 attribute id uses an identifier to identify each unique instance of a clinical document.
		IINPfITUuidMandatory id = new IINPfITUuidMandatory();
		id.setRoot(uuidGenerator.random());
		document.setId(id);

		// The HL7 (NHS localisation) attribute messageType identifies the CDA document as one that complies with a certain NHS CDA profile.
		MessageType messageType = new MessageType();
		messageType.setRoot("2.16.840.1.113883.2.1.3.2.4.18.17");
		messageType.setExtension("POCD_MT200001GB02");
		document.setMessageType(messageType);

		IINPfITUuidMandatory setId = new IINPfITUuidMandatory();
		setId.setRoot(uuidGenerator.random());
		document.setSetId(setId);
		document.setTitle("Integrated Urgent Care Report");

		// The HL7 attribute typeId signals the imposition of constraints defined in an HL7-specified message type.
		POCDMT200001GB02ClinicalDocumentTypeId typeId = new POCDMT200001GB02ClinicalDocumentTypeId();
		typeId.setRoot("2.16.840.1.113883.1.3");
		typeId.setExtension("POCD_HD000040");
		document.setTypeId(typeId);

		// The HL7 attribute versionNumber uses an integer value to allow versioning of the CDA document.
		VersionNumber versionNumber = new VersionNumber();
		versionNumber.setValue(new BigInteger("1"));
		document.setVersionNumber(versionNumber);
	}

}
