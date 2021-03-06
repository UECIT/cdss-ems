import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Patient } from '../model/patient';
import { environment } from '../../environments/environment';
import { SessionStorage } from 'h5webstorage';

const httpOptions = {
  headers: new HttpHeaders()
};

@Injectable({
  providedIn: 'root'
})
export class PatientService {
  constructor(private http: HttpClient, private sessionStorage: SessionStorage) {}

  getAllPatients(): Observable<Patient[]> {
    if (this.sessionStorage['auth_token'] != null) {
      httpOptions.headers = httpOptions.headers.set(
        'Authorization',
        this.sessionStorage['auth_token']
      );
      const url = `${environment.EMS_API}/patient/all`;
      return this.http.get<Patient[]>(url, httpOptions).pipe();
    }
  }

  getPatient(patientId: string, encounterId: string) {
    let params = new HttpParams()
      .set("patientRef", patientId)
      .set("encounterRef", encounterId);


    if (this.sessionStorage['auth_token'] != null) {
      httpOptions.headers = httpOptions.headers.set(
        'Authorization',
        this.sessionStorage['auth_token']
      );
      const url = `${environment.EMS_API}/patient?${params.toString()}`;
      return this.http.get<Patient>(url, httpOptions).pipe();
    }
  }
}
