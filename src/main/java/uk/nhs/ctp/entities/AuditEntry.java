package uk.nhs.ctp.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString.Exclude;
import uk.nhs.ctp.audit.HttpAudit;

@Entity
@Table(name = "audit_entry_v2")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AuditEntry implements HttpAudit {

  // generic audit details
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne(fetch = FetchType.EAGER)
  @JoinColumn(name = "audit_id")
  @JsonIgnore
  @Exclude
  @EqualsAndHashCode.Exclude
  private Audit audit;

  @Temporal(TemporalType.TIMESTAMP)
  @Column(name = "created_date")
  private Date createdDate;

  @Column(name = "request_method")
  @Lob
  private String requestMethod;

  @Column(name = "request_url")
  @Lob
  private String requestUrl;

  @Column(name = "request_headers")
  @Lob
  private String requestHeaders;

  @Column(name = "request_body")
  @Lob
  private String requestBody;

  @Column(name = "response_status")
  private int responseStatus;

  @Column(name = "response_status_text")
  private String responseStatusText;

  @Column(name = "response_headers")
  @Lob
  private String responseHeaders;

  @Column(name = "response_body")
  @Lob
  private String responseBody;

  @Column(name = "data")
  @Lob
  private byte[] data;

}
