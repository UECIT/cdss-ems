package uk.nhs.ctp.service.fhir;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.util.FhirTerser;
import java.util.Arrays;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.hl7.fhir.dstu3.model.IdType;
import org.hl7.fhir.dstu3.model.Reference;
import org.hl7.fhir.dstu3.model.ResourceType;
import org.hl7.fhir.instance.model.api.IBaseReference;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.IIdType;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriComponentsBuilder;

/**
 * Cleanly construct IDs for Resources served by the local FHIR endpoint
 */
@Service
@RequiredArgsConstructor
public class ReferenceService {

  @Value("${ems.fhir.server}")
  private String emsServer;

  private final FhirContext fhirContext;

  public String buildId(ResourceType resourceType, long id) {
    return buildId(resourceType, Long.toString(id));
  }

  public String buildId(ResourceType resourceType, String id) {
    return buildId(emsServer, resourceType, id);
  }

  public String buildId(String base, ResourceType resourceType, String id) {
    var idType = new IdType(id);
    if (idType.isAbsolute()) {
      return id;
    }

    var resourceSegment = StringUtils.isEmpty(idType.getResourceType())
        ? resourceType.name()
        : idType.getResourceType();

    return UriComponentsBuilder.fromUriString(base)
        .pathSegment(resourceSegment, idType.getIdPart())
        .build()
        .toUriString();
  }

  /**
   * @param idElement IdType
   * @return the value of the id element, or falls back to a local EMS ID if no base URL is given
   */
  public String buildId(IIdType idElement) {
    return idElement.isAbsolute()
        ? idElement.getValue()
        : buildId(ResourceType.fromCode(idElement.getResourceType()), idElement.getIdPart());
  }

  public Reference buildRef(ResourceType resourceType, long id) {
    return new Reference(buildId(resourceType, id));
  }

  public Reference buildRef(ResourceType resourceType, String id) {
    return new Reference(buildId(resourceType, id));
  }

  public Reference buildRef(IIdType idElement) {
    return new Reference(buildId(idElement));
  }

  public Reference buildRef(String baseUrl, ResourceType resourceType, String id) {
    return new Reference(buildId(baseUrl, resourceType, id));
  }

  public void resolve(String baseUrl, List<? extends IBaseReference> references) {
    for (IBaseReference ref : references) {
      IIdType id = ref.getReferenceElement();
      if (!id.isAbsolute()) {
        ref.setReference(id.withServerBase(baseUrl, id.getResourceType()).toString());
      }
    }
  }

  public void resolve(String baseUrl, IBaseReference... references) {
    resolve(baseUrl, Arrays.asList(references));
  }

  public void resolveRelative(String baseUrl, IBaseResource resource) {
    FhirTerser fhirTerser = fhirContext.newTerser();
    fhirTerser.getAllResourceReferences(resource).forEach(resourceReferenceInfo ->
        resolve(baseUrl, resourceReferenceInfo.getResourceReference())
    );
  }
}
