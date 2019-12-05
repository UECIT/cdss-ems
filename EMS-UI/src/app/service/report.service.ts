import { Injectable } from '@angular/core';
import { HttpHeaders, HttpClient } from '@angular/common/http';
import { environment } from '../../environments/environment';
import { SessionStorage } from 'h5webstorage';

@Injectable({
  providedIn: 'root'
})
export class ReportService {

  constructor(private http: HttpClient, private sessionStorage: SessionStorage) { }

  getHandover(caseId: any, resourceUrl: any) {
    const httpOptions = {
      headers: new HttpHeaders()
    };
    if (this.sessionStorage['auth_token'] != null) {
      httpOptions.headers = httpOptions.headers.set(
        'Authorization',
        this.sessionStorage['auth_token']
      );
      const url = `${environment.EMS_API}/handover`;
      const handoverRequest = {'resourceUrl': resourceUrl, 'caseId': caseId};
      return this.http.post<any>(url, handoverRequest, httpOptions).toPromise();
    }
  }

  postHandoverTemplate(handoverMessage) {
    const httpOptions = {
      headers: new HttpHeaders()
    };
    httpOptions.headers = httpOptions.headers.set(
      'Content-Type',
      'application/json'
    );
    return this.http.post<any>(`${environment.UECDI_API}/handover`, handoverMessage, httpOptions).toPromise();
  }

  getReport(caseId: any, resourceUrl: any, handoverMessage: any) {
    const httpOptions = {
      headers: new HttpHeaders()
    };
    if (this.sessionStorage['auth_token'] != null) {
      httpOptions.headers = httpOptions.headers.set(
        'Authorization',
        this.sessionStorage['auth_token']
      );
      const url = `${environment.EMS_API}/report`;
      const handoverRequest = {'handoverJson': JSON.stringify(handoverMessage), 'caseId': caseId};
      return this.http.post<any>(url, handoverRequest, httpOptions).toPromise();
    }
  }

  validate111ReportV2(oneOneOneReportXml: any) {
    const httpOptions = {
      headers: new HttpHeaders(),
      responseType: 'text' as 'json'
    };
    httpOptions.headers = httpOptions.headers.set(
      'Content-Type',
      'application/xml'
    );
    const url = `${environment.UECDI_VALIDATE_API}/validate/111v2`;
    return this.http.post<any>(url, oneOneOneReportXml, httpOptions).toPromise();
  }

  validate111ReportV3(oneOneOneReportXml: any) {
    const httpOptions = {
      headers: new HttpHeaders(),
      responseType: 'text' as 'json'
    };
    httpOptions.headers = httpOptions.headers.set(
      'Content-Type',
      'application/xml'
    );
    const url = `${environment.UECDI_VALIDATE_API}/validate/111v3`;
    return this.http.post<any>(url, oneOneOneReportXml, httpOptions).toPromise();
  }

  validateAmbulanceRequestV2(oneOneOneReportXml: any) {
    const httpOptions = {
      headers: new HttpHeaders(),
      responseType: 'text' as 'json'
    };
    httpOptions.headers = httpOptions.headers.set(
      'Content-Type',
      'application/xml'
    );
    const url = `${environment.UECDI_VALIDATE_API}/validate/ambulancev2`;
    return this.http.post<any>(url, oneOneOneReportXml, httpOptions).toPromise();
  }

  validateAmbulanceRequestV3(ambulanceRequestXml: any) {
    const httpOptions = {
      headers: new HttpHeaders(),
      responseType: 'text' as 'json'
    };
    httpOptions.headers = httpOptions.headers.set(
      'Content-Type',
      'application/xml'
    );
    const url = `${environment.UECDI_VALIDATE_API}/validate/ambulancev3`;
    return this.http.post<any>(url, ambulanceRequestXml, httpOptions).toPromise();

  }
}