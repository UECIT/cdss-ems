package uk.nhs.ctp.controllers;

import java.util.List;
import javax.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import uk.nhs.ctp.entities.CdssSupplier;
import uk.nhs.ctp.service.CdssService;
import uk.nhs.ctp.service.CdssSupplierService;
import uk.nhs.ctp.service.dto.CdssSupplierDTO;
import uk.nhs.ctp.service.dto.NewCdssSupplierDTO;
import uk.nhs.ctp.service.dto.ServiceDefinitionDTO;
import uk.nhs.ctp.service.search.SearchParameters;

@CrossOrigin
@RestController
@RequestMapping(path = "/cdss")
public class CdssController {

  private final Logger log = LoggerFactory.getLogger(getClass());

  private final CdssSupplierService cdssSupplierService;
  private final CdssService cdssService;

  public CdssController(
      CdssSupplierService cdssSupplierService, CdssService cdssService) {
    this.cdssSupplierService = cdssSupplierService;
    this.cdssService = cdssService;
  }

  @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_NHS','ROLE_CDSS')")
  @GetMapping
  public @ResponseBody
  List<CdssSupplierDTO> getCdssSuppliers(HttpServletRequest request) {
    return cdssSupplierService.getCdssSuppliers(request.getUserPrincipal().getName());
  }

  @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
  @GetMapping(params = "admin")
  public @ResponseBody
  List<CdssSupplier> getCdssSuppliers(@RequestParam(name = "admin") Boolean admin,
      HttpServletRequest request) {
    return cdssSupplierService.getCdssSuppliersUnfiltered(request.getUserPrincipal().getName());
  }

  @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_NHS','ROLE_CDSS')")
  @GetMapping("/{id}")
  public @ResponseBody
  CdssSupplier getCdssSupplier(@PathVariable Long id, HttpServletRequest request) {
    return cdssSupplierService.getCdssSupplier(id);
  }

  @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_NHS','ROLE_CDSS')")
  @GetMapping("/{id}/ServiceDefinition")
  public @ResponseBody
  List<ServiceDefinitionDTO> getServiceDefinitions(@PathVariable Long id,
      HttpServletRequest request) {
    CdssSupplier cdssSupplier = cdssSupplierService.getCdssSupplier(id);
    CdssSupplierDTO cdssSupplierDTO = cdssService
        .queryServiceDefinitions(cdssSupplier, SearchParameters.builder().build());
    return cdssSupplierDTO.getServiceDefinitions();
  }

  @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
  @PostMapping
  public @ResponseBody
  CdssSupplier createCdssSupplier(@RequestBody NewCdssSupplierDTO cdssSupplier,
      HttpServletRequest request) {
    return cdssSupplierService.createCdssSupplier(cdssSupplier);
  }

  @PreAuthorize("hasRole('ROLE_ADMIN')")
  @PutMapping
  public @ResponseBody
  CdssSupplier updateCdssSupplier(@RequestBody CdssSupplier cdssSupplier) throws Exception {
    return cdssSupplierService.updateCdssSupplier(cdssSupplier);
  }

  @PreAuthorize("hasRole('ROLE_ADMIN')")
  @DeleteMapping(path = "/{id}")
  public @ResponseBody
  void deleteCdssSupplier(@PathVariable("id") Long id) throws Exception {
    cdssSupplierService.deleteCdssSupplier(id);
  }
}
