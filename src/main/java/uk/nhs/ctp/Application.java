package uk.nhs.ctp;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.boot.web.support.SpringBootServletInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;
import org.springframework.validation.beanvalidation.MethodValidationPostProcessor;
import org.springframework.web.client.RestTemplate;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.parser.IParser;
import resources.CareConnectOrganization;
import resources.CareConnectPatient;
import resources.CareConnectPractitioner;
import uk.nhs.ctp.service.report.org.hl7.v3.CDAOrganizationTypeDisplayName;

@ServletComponentScan
@SpringBootApplication
@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true)
public class Application extends SpringBootServletInitializer {

	@Override
	protected SpringApplicationBuilder configure(final SpringApplicationBuilder builder) {
		return builder.sources(Application.class);
	}

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}

	@Bean
	public MethodValidationPostProcessor methodValidationPostProcessor() {
		MethodValidationPostProcessor processor = new MethodValidationPostProcessor();
		processor.setValidator(validator());

		return processor;
	}

	@Bean
	public LocalValidatorFactoryBean validator() {
		return new LocalValidatorFactoryBean();
	}

	@Bean
	public RestTemplate restTemplate(RestTemplateBuilder builder) {
		return builder.build();
	}
	
	@Bean
	public IParser fhirParser() {
		IParser fhirParser = fhirContext().newJsonParser();
		fhirParser.setPreferTypes(Arrays.asList(
				CareConnectPatient.class, CareConnectPractitioner.class, CareConnectOrganization.class));
		
		return fhirParser;
	}
	
	@Bean
	public FhirContext fhirContext() {
		return FhirContext.forDstu3();
	}
	
	@Bean
	public Map<String, CDAOrganizationTypeDisplayName> organizationTypeMap() {
		Map<String, CDAOrganizationTypeDisplayName> organizationTypeMap = 
				new HashMap<String, CDAOrganizationTypeDisplayName>();
		
		organizationTypeMap.put("http://terminology.hl7.org/CodeSystem/organization-typeprov", 
				CDAOrganizationTypeDisplayName.GENERAL_MEDICAL_PRACTICE);
		
		return organizationTypeMap;
	}

}
