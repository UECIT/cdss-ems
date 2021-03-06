package uk.nhs.ctp.config;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.model.api.annotation.ResourceDef;
import ca.uhn.fhir.rest.client.apache.ApacheRestfulClientFactory;
import ca.uhn.fhir.rest.client.api.IClientInterceptor;
import ca.uhn.fhir.rest.client.api.IGenericClient;
import java.util.Arrays;
import java.util.List;
import javax.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.hl7.fhir.dstu3.model.CareConnectCarePlan;
import org.hl7.fhir.dstu3.model.CareConnectCareTeam;
import org.hl7.fhir.dstu3.model.CareConnectEncounter;
import org.hl7.fhir.dstu3.model.CareConnectEpisodeOfCare;
import org.hl7.fhir.dstu3.model.CareConnectHealthcareService;
import org.hl7.fhir.dstu3.model.CareConnectLocation;
import org.hl7.fhir.dstu3.model.CareConnectMedication;
import org.hl7.fhir.dstu3.model.CareConnectMedicationRequest;
import org.hl7.fhir.dstu3.model.CareConnectObservation;
import org.hl7.fhir.dstu3.model.CareConnectOrganization;
import org.hl7.fhir.dstu3.model.CareConnectPatient;
import org.hl7.fhir.dstu3.model.CareConnectPractitioner;
import org.hl7.fhir.dstu3.model.CareConnectProcedure;
import org.hl7.fhir.dstu3.model.CareConnectProcedureRequest;
import org.hl7.fhir.dstu3.model.CareConnectRelatedPerson;
import org.hl7.fhir.dstu3.model.CareConnectSpecimen;
import org.hl7.fhir.dstu3.model.CoordinateResource;
import org.hl7.fhir.dstu3.model.Resource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class FHIRConfig {

  private final List<IClientInterceptor> clientInterceptors;

  @Bean
  public FhirContext fhirContext() {
    FhirContext fhirContext = FhirContext.forDstu3();

    List<Class<? extends Resource>> profiles = Arrays.asList(
        CareConnectCarePlan.class,
        CareConnectCareTeam.class,
        CareConnectEncounter.class,
        CareConnectEpisodeOfCare.class,
        CareConnectHealthcareService.class,
        CareConnectLocation.class,
        CareConnectMedication.class,
        CareConnectMedicationRequest.class,
        CareConnectObservation.class,
        CareConnectOrganization.class,
        CareConnectPatient.class,
        CareConnectPractitioner.class,
        CareConnectProcedure.class,
        CareConnectProcedureRequest.class,
        CareConnectRelatedPerson.class,
        CareConnectSpecimen.class);

    for (Class<? extends Resource> profileClass : profiles) {
      ResourceDef resourceDef = profileClass.getAnnotation(ResourceDef.class);
      String profile = resourceDef.profile();
      fhirContext.setDefaultTypeForProfile(profile, profileClass);
    }

    fhirContext.registerCustomType(CoordinateResource.class);

    return fhirContext;
  }

  @PostConstruct
  private void configureClientInterceptors() {
    ApacheRestfulClientFactory factory = new ApacheRestfulClientFactory() {
      @Override
      public synchronized IGenericClient newGenericClient(String theServerBase) {
        IGenericClient client = super.newGenericClient(theServerBase);

        for (IClientInterceptor interceptor : clientInterceptors) {
          client.registerInterceptor(interceptor);
        }
        return client;
      }
    };

    FhirContext fhirContext = fhirContext();
    factory.setFhirContext(fhirContext);
    fhirContext.setRestfulClientFactory(factory);
  }
}
