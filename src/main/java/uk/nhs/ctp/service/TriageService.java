package uk.nhs.ctp.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import java.net.ConnectException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hl7.fhir.dstu3.model.IdType;
import org.hl7.fhir.dstu3.model.Questionnaire;
import org.hl7.fhir.exceptions.FHIRException;
import org.springframework.stereotype.Service;
import uk.nhs.ctp.entities.AuditRecord;
import uk.nhs.ctp.entities.CaseObservation;
import uk.nhs.ctp.service.dto.CdssRequestDTO;
import uk.nhs.ctp.service.dto.CdssResponseDTO;
import uk.nhs.ctp.service.dto.CdssResult;
import uk.nhs.ctp.service.dto.EncounterReportInput;
import uk.nhs.ctp.service.dto.TriageLaunchDTO;
import uk.nhs.ctp.service.dto.TriageQuestion;
import uk.nhs.ctp.transform.CaseObservationTransformer;
import uk.nhs.ctp.utils.ResourceProviderUtils;

@Service
@AllArgsConstructor
@Slf4j
public class TriageService {

  private CaseService caseService;
  private CdssService cdssService;
  private ResponseService responseService;
  private AuditService auditService;
  private EncounterService encounterService;
  private EvaluateService evaluateService;
  private CaseObservationTransformer caseObservationTransformer;

  /**
   * Creates case from test case scenario and patient details and launches first triage request
   *
   * @param requestDetails {@link TriageLaunchDTO}
   * @return response {@link CdssResponseDTO}
   * @throws JsonProcessingException
   */
  public CdssResponseDTO launchTriage(TriageLaunchDTO requestDetails) throws Exception {

    Long caseId = caseService.createCase(
        requestDetails.getPatientId(),
        requestDetails.getSettings().getPractitioner().getId()
    ).getId();
    String encounterId = requestDetails.getEncounterId();
    if (encounterId != null) {
      log.info("Continuing triage journey for encounter {}", encounterId);
      updateCaseFromEncounterReport(caseId, encounterId);
    }

    CdssRequestDTO cdssRequest = new CdssRequestDTO();
    cdssRequest.setCaseId(caseId);
    cdssRequest.setCdssSupplierId(requestDetails.getCdssSupplierId());
    cdssRequest.setServiceDefinitionId(requestDetails.getServiceDefinitionId());
    cdssRequest.setSettings(requestDetails.getSettings());
    cdssRequest.setPatientId(requestDetails.getPatientId());

    return processTriageRequest(cdssRequest);
  }

  private void updateCaseFromEncounterReport(Long caseId, String encounterId) {
    //TODO: This could be moved out when we have more than observations
    //TODO: Ideally we cached the ER somewhere, for now we fetch it again
    EncounterReportInput encounterReportInput = encounterService
        .getEncounterReport(new IdType(encounterId));
    encounterReportInput.getObservations().forEach(obs -> {
      CaseObservation caseObservation = caseObservationTransformer.transform(obs);
      caseService.addObservation(caseId, caseObservation);
    });
  }

  /**
   * Processes triage request and returns a summary of the response
   *
   * @param requestDetails {@link CdssRequestDTO}
   * @return response {@link CdssResponseDTO}
   * @throws JsonProcessingException
   */
  public CdssResponseDTO processTriageRequest(CdssRequestDTO requestDetails) throws Exception {

    Long caseId = requestDetails.getCaseId();

    // start audit
    AuditRecord auditRecord = auditService.createNewAudit(caseId);

    CdssResult cdssResult =  evaluateService.evaluate(requestDetails);

    CdssResponseDTO cdssResponse = buildResponseDtoFromResult(cdssResult,
        caseId,
        requestDetails.getCdssSupplierId());

    auditService
        .updateAuditEntry(auditRecord, requestDetails, cdssResponse, cdssResult.getContained());

    if (cdssResult.isInProgress()) {
      requestDetails.setQuestionResponse(null);
      cdssResult =  evaluateService.evaluate(requestDetails);

      // Add Audit Record
      cdssResponse = buildResponseDtoFromResult(cdssResult, caseId,
          requestDetails.getCdssSupplierId());
      auditService
          .updateAuditEntry(auditRecord, requestDetails, cdssResponse, cdssResult.getContained());
    }

    return cdssResponse;
  }

  /**
   * Processes triage amend request and returns a summary of the response
   *
   * @param requestDetails {@link CdssRequestDTO}
   * @return response {@link CdssResponseDTO}
   * @throws JsonProcessingException
   */
  public CdssResponseDTO processTriageAmendRequest(CdssRequestDTO requestDetails)
      throws Exception {

    Long caseId = requestDetails.getCaseId();

    // start audit
    AuditRecord auditRecord = auditService.createNewAudit(caseId);
    CdssResult cdssResult = evaluateService.evaluate(requestDetails);

    // Add Audit Record
    CdssResponseDTO cdssResponse = buildAmendResponseDtoFromResult(
        cdssResult, caseId, requestDetails.getCdssSupplierId(),
        requestDetails.getQuestionResponse());
    auditService
        .updateAuditEntry(auditRecord, requestDetails, cdssResponse, cdssResult.getContained());

    return cdssResponse;
  }

  /**
   * Builds a responseDTO from a cdssResult
   *
   * @param cdssResult {@link CdssResult}
   * @param caseId     {@link Long}
   * @return {@link CdssRequestDTO}
   * @throws JsonProcessingException
   */
  protected CdssResponseDTO buildResponseDtoFromResult(CdssResult cdssResult, Long caseId,
      Long cdssSupplierId)
      throws ConnectException, JsonProcessingException, FHIRException {
    if (cdssResult == null) {
      throw new NullPointerException("CdssResult is empty");
    }
    Questionnaire questionnaire = null;
    if (!cdssResult.hasResult() && cdssResult.hasQuestionnaire()) {
      // TODO GetResource out of contained
      questionnaire = ResourceProviderUtils
          .getResource(cdssResult.getContained(), Questionnaire.class);
      questionnaire = questionnaire == null ? cdssService
          .getQuestionnaire(cdssSupplierId, cdssResult.getQuestionnaireRef(), caseId)
          : questionnaire;
    }
    return responseService.buildResponse(cdssResult, questionnaire, caseId, cdssSupplierId);
  }

  /**
   * Builds an amended responseDTO from a cdssResult
   *
   * @param cdssResult        {@link CdssResult}
   * @param caseId            {@link Long}
   * @param previousQuestions
   * @return {@link CdssRequestDTO}
   * @throws JsonProcessingException
   */
  protected CdssResponseDTO buildAmendResponseDtoFromResult(CdssResult cdssResult, Long caseId,
      Long cdssSupplierId,
      TriageQuestion[] previousQuestions)
      throws ConnectException, JsonProcessingException, FHIRException {
    if (cdssResult == null) {
      throw new NullPointerException("CdssResult is empty");
    }
    Questionnaire questionnaire = null;

    if (!cdssResult.hasResult() && cdssResult.hasQuestionnaire()) {
      questionnaire = cdssService.getQuestionnaire(cdssSupplierId,
          "Questionnaire/" + previousQuestions[0].getQuestionnaireId(), caseId);
    }
    return responseService
        .buildAmendResponse(cdssResult, questionnaire, caseId, cdssSupplierId, previousQuestions);
  }
}
