package uk.nhs.ctp.controllers;

import java.util.Collection;

import javax.xml.bind.JAXBException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;

import ca.uhn.fhir.parser.IParser;
import uk.nhs.ctp.service.ReportService;
import uk.nhs.ctp.service.dto.ReportRequestDTO;
import uk.nhs.ctp.service.dto.ReportsDTO;

@CrossOrigin
@RestController
@RequestMapping(path = "/report")
public class ReportController {
	
	@Autowired
	private ReportService reportService;
	
	@Autowired
	private IParser fhirParser;
	
	@PostMapping
	public @ResponseBody Collection<ReportsDTO> getReport(
			@RequestBody ReportRequestDTO reportRequestDTO) throws JAXBException, JsonProcessingException {
		
		reportRequestDTO.setFhirParser(fhirParser);
		return reportService.generateReports(reportRequestDTO);
	}

}