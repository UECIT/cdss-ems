package uk.nhs.ctp.config.interceptors;

import java.io.IOException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.stereotype.Component;
import uk.nhs.ctp.service.AuditService;
import uk.nhs.ctp.audit.HttpResponse;

@Component
@RequiredArgsConstructor
public class AuditRestClient implements ClientHttpRequestInterceptor {

  private final AuditService auditService;

  @Override
  public ClientHttpResponse intercept(HttpRequest request, byte[] body,
      ClientHttpRequestExecution execution) throws IOException {

    auditService.startEntry(uk.nhs.ctp.audit.HttpRequest.from(request, body));

    ClientHttpResponse response = execution.execute(request, body);

    auditService.endEntry(HttpResponse.from(response));

    return response;
  }
}
