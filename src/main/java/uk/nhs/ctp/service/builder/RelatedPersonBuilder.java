package uk.nhs.ctp.service.builder;

import java.util.Date;

import org.hl7.fhir.dstu3.model.Address.AddressType;
import org.hl7.fhir.dstu3.model.Address.AddressUse;
import org.hl7.fhir.dstu3.model.ContactPoint.ContactPointSystem;
import org.hl7.fhir.dstu3.model.ContactPoint.ContactPointUse;
import org.hl7.fhir.dstu3.model.Period;
import org.hl7.fhir.dstu3.model.RelatedPerson;
import org.springframework.stereotype.Component;

@Component
public class RelatedPersonBuilder {

	public RelatedPerson build() {
		RelatedPerson person = new RelatedPerson();
		person.addAddress()
			.setUse(AddressUse.HOME)
			.setType(AddressType.BOTH)
			.addLine("1 Lovely View")
			.addLine("High Street")
			.setCity("Sheffield")
			.setDistrict("South Yorkshire")
			.setPostalCode("S1 4LL")
			.setCountry("United Kingdom")
			.setPeriod(new Period().setStart(new Date()).setEnd(new Date()));
		
		person.addName()
			.addGiven("Charles")
			.addPrefix("Mr")
			.setFamily("Ingram");
		
		person.addTelecom()
			.setSystem(ContactPointSystem.PHONE)
			.setValue("01904 134 543")
			.setUse(ContactPointUse.HOME)
			.setRank(1)
			.setPeriod(new Period().setStart(new Date()).setEnd(new Date()));
		
		return person;
	}
}
