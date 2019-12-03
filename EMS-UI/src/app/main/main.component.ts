import {Component, OnInit} from '@angular/core';
import {Router} from '@angular/router';
import {Patient} from '../model/patient';
import {PatientService} from '../service/patient.service';
import {Store} from '@ngrx/store';
import {AppState} from '../app.state';
import * as PatientActions from '../actions/patient.actions';
import {CdssService} from '../service/cdss.service';
import {CdssSupplier, ServiceDefinition} from '../model/cdssSupplier';
import {MatSnackBar} from '@angular/material';
import {SessionStorage} from 'h5webstorage';
import {SelectService} from '../model/selectService';
import {TriageService} from '../service/triage.service';
import {ToastrService} from 'ngx-toastr';

@Component({
  selector: 'app-main',
  templateUrl: './main.component.html',
  styleUrls: ['./main.component.css']
})
export class MainComponent implements OnInit {
  items = [{text: 'Manage Users'}, {text: 'Settings'}];

  patients: Patient[];
  selectedPatient: Patient;
  cdssSuppliers: CdssSupplier[];
  serviceDefinitions: ServiceDefinition[];
  selectedSupplier: number;
  selectedServiceDefinition: string;
  displayedTestWarningMessage = false;
  selectedQueryType = 'id';
  serviceDefinitionMode = 'automated';
  availableServiceDefinitions: CdssSupplier[];

  constructor(
      public router: Router,
      private patientService: PatientService,
      private store: Store<AppState>,
      private cdssSupplierService: CdssService,
      private triageService: TriageService,
      public snackBar: MatSnackBar,
      private sessionStorage: SessionStorage,
      private toastr: ToastrService
  ) {
  }

  disableLaunch() {
    return !(
        this.selectedPatient != null &&
        this.selectedSupplier != null &&
        this.selectedServiceDefinition != null
    );
  }

  ngOnInit() {
    this.getPatients();
    this.getCdssSuppliers();
    this.autoSelectServiceDefinition(false);
    // this.openSnackBar();
    console.log(this.sessionStorage['displayedTestWarningMessage']);
    if (
        this.sessionStorage['displayedTestWarningMessage'] === 'false' ||
        this.sessionStorage['displayedTestWarningMessage'] == null
    ) {
      setTimeout(() =>
          this.openSnackBar(
              'This Test Harness is for demonstration purposes only and is not representative of any EMS final product.'
          )
      );
      this.sessionStorage.setItem('displayedTestWarningMessage', 'true');
    }

    this.sessionStorage.setItem('triageItems', '[]');
  }

  openSnackBar(message) {
    this.snackBar.open(message, 'I Understand');
  }

  getPatients() {
    this.patientService
    .getAllPatients()
    .subscribe(patients => (this.patients = patients));
  }

  triage() {
    this.sessionStorage.setItem(
        'serviceDefinitionId',
        this.selectedServiceDefinition.toString()
    );
    this.sessionStorage.setItem(
        'cdssSupplierId',
        this.selectedSupplier.toString()
    );
    this.router.navigate(['/triage']);
  }

  addPatientToStore(patient: Patient) {
    this.selectedPatient = patient;
    this.store.dispatch(new PatientActions.AddPatient(patient));
  }

  async getCdssSuppliers() {
    this.cdssSuppliers =
        await this.cdssSupplierService.getCdssSuppliers().toPromise();
  }

  async setSelectedSupplier(supplier: CdssSupplier) {
    this.selectedSupplier = supplier.id;
    this.addSupplierToStore(supplier);

    // Request list of SDs from CDSS
    this.serviceDefinitions = await this.cdssSupplierService.listServiceDefinitions(supplier.id);
  }

  addSupplierToStore(supplier: CdssSupplier) {
    this.sessionStorage.setItem('cdssSupplierName', supplier.name);
  }

  async autoSelectServiceDefinition(force: boolean) {
    if (this.serviceDefinitionMode === 'automated' || force) {
      this.serviceDefinitionMode = 'automated';

      // Request available SDs for the current patient (or no known patient)
      const request = new SelectService();
      if (this.selectedPatient) {
        request.patientId = this.selectedPatient.id;
      }
      const selectedSDs = await this.triageService.selectServiceDefinitions(request);

      if (selectedSDs.length > 0) {
        this.availableServiceDefinitions = selectedSDs;
      } else {
        this.toastr.info('No available service definitions');
      }
    }
  }

  selectServiceDefinition(serviceDefinition: ServiceDefinition) {
    this.selectedServiceDefinition = serviceDefinition.serviceDefinitionId;
  }
}
