import { ReportService } from 'src/app/service/report.service';
import {Component, OnInit} from '@angular/core';
import {Router, ActivatedRoute} from '@angular/router';
import {MatSnackBar} from '@angular/material';
import {Store} from '@ngrx/store';
import {ToastrService} from 'ngx-toastr';
import {SessionStorage} from 'h5webstorage';
import {
  Patient,
  CdssSupplier,
  ServiceDefinition,
  SelectService,
  Code,
  Settings,
  Practitioner,
  EncounterReportInput
} from '../model';
import {PatientService, CdssService, TriageService, PractitionerService} from '../service';
import {AppState} from '../app.state';
import * as PatientActions from '../actions/patient.actions';


@Component({
  selector: 'app-main',
  templateUrl: './main.component.html',
  styleUrls: ['./main.component.css']
})
export class MainComponent implements OnInit {
  items = [{text: 'Manage Users'}, {text: 'Settings'}];

  patients: Patient[];
  practitioners: Practitioner[];
  selectedPatient: Patient;
  cdssSuppliers: CdssSupplier[];
  serviceDefinitions: ServiceDefinition[];
  selectedSupplier: number;
  selectedServiceDefinition: string;
  displayedTestWarningMessage = false;
  selectedQueryType = 'id';
  serviceDefinitionMode = 'automated';
  availableServiceDefinitions: CdssSupplier[];
  roles: Code[];
  selectedRole: string;
  settings: Code[];
  jurisdictions: Code[];
  selectionModeOptions: any[];
  encounterReportInput: EncounterReportInput;

  constructor(
      public router: Router,
      private store: Store<AppState>,
      private practitionerService: PractitionerService,
      public snackBar: MatSnackBar,
      private sessionStorage: SessionStorage,
      private toastr: ToastrService,
      private route: ActivatedRoute,
      private cdssSupplierService: CdssService,
      private triageService: TriageService,
      private patientService: PatientService,
      private reportService: ReportService
  ) {
  }

  disableLaunch() {
    return this.selectedPatient == null ||
           this.selectedSupplier == null ||
           this.selectedServiceDefinition == null;
  }

  ngOnInit() {
    let encounterId = this.route.snapshot.queryParamMap.get("encounterId");
    if (encounterId) {
      this.getEncounterReport(encounterId)
        .then(er => {
          this.encounterReportInput = er;
          this.sessionStorage.setItem('encounterHandover', JSON.stringify(er));
          this.getPatients(this.encounterReportInput.patientId, this.encounterReportInput.encounterId);
        });

    }
    else {
      this.sessionStorage.removeItem("encounterHandover");
      this.getPatients()
    }
    this.getCdssSuppliers();
    this.getRoles();
    this.getSettings();
    this.getJurisdictions();
    this.getPractitioners();
    this.getSelectionModeOptions();

    var settings: Settings = this.sessionStorage['settings'];
    settings.jurisdiction = this.jurisdictions[0];
    settings.setting = this.settings[0];
    settings.userType = this.roles[0];
    this.sessionStorage.setItem('settings', JSON.stringify(settings));

    this.autoSelectServiceDefinition(false);
    this.openSnackBar();

    this.sessionStorage.setItem('triageItems', '[]');
  }

  getEncounterReport(encounterId) {
    return this.reportService.getEncounterReport(encounterId);
  }

  openSnackBar() {
    var hasDisplayed = this.sessionStorage['displayedTestWarningMessage']
    if (hasDisplayed === 'false' || hasDisplayed == null) {
      setTimeout(() => 
        this.snackBar.open('This Test Harness is for demonstration purposes only and is not representative of any EMS final product.', 'I Understand'));
    }
    this.sessionStorage.setItem('displayedTestWarningMessage', 'true');
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

  async getPatients(patientId?: string, encounterId?: string) {
    this.patients = patientId && encounterId
      ? [await this.patientService.getPatient(patientId, encounterId).toPromise()]
      : await this.patientService.getAllPatients().toPromise();

    this.selectedPatient = this.patients[0];
    this.store.dispatch(new PatientActions.AddPatient(this.selectedPatient));
  }

  async getPractitioners() {
    this.practitioners = await this.practitionerService.getAllPractitioners().toPromise();
  }

  async getCdssSuppliers() {
    this.cdssSuppliers = await this.cdssSupplierService.getCdssSuppliers().toPromise();
  }

  getRoles() {
    this.roles = [
      {
        'id': 1,
        'description': 'Patient',
        'code': 'Patient',
        'display': 'Patient'
      },
      {
        'id': 2,
        'description': 'Related Person',
        'code': 'RelatedPerson',
        'display': 'Related Person'
      },
      {
        'id': 3,
        'description': 'Practitioner',
        'code': 'Practitioner',
        'display': 'Practitioner'
      }
    ];
  }

  getSettings() {
    this.settings = [
      {
        'id': 1,
        'description': 'Online',
        'code': 'online',
        'display': 'Online'
      },
      {
        'id': 2,
        'description': 'Phone call',
        'code': 'phone',
        'display': 'Phone call'
      },
      {
        'id': 3,
        'description': 'Clinical',
        'code': 'clinical',
        'display': 'Clinical'
      }
    ];
  }

  getJurisdictions() {
    this.jurisdictions = [
      {
        'id': 1,
        'description': 'United Kingdom of Great Britain and Northern Ireland (the)',
        'code': 'GB',
        'display': 'United Kingdom of Great Britain and Northern Ireland (the)'
      },
      {
        'id': 2,
        'description': 'Tokelau',
        'code': 'TK',
        'display': 'Tokelau'
      }
    ];
  }

  getSelectionModeOptions() {
    this.selectionModeOptions = [
      {
        'id':'automated',
        'display': 'Automated'
      },
      {
        'id':'manual',
        'display': 'Manual'
      }
    ];
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

  addRoleToStore(role: Code) {
    const settings: Settings = this.sessionStorage['settings'];
    settings.userType = role;
    this.sessionStorage.setItem('settings', JSON.stringify(settings));
    this.selectedRole = role.code;
    this.autoSelectServiceDefinition(false);
  }

  addSettingToStore(setting: Code) {
    var settings: Settings = this.sessionStorage['settings'];
    settings.setting = setting;
    this.sessionStorage.setItem('settings', JSON.stringify(settings));
    this.autoSelectServiceDefinition(false);
  }

  addJurisdictionToStore(jurisdiction: Code) {
    var settings: Settings = this.sessionStorage['settings'];
    settings.jurisdiction = jurisdiction;
    this.sessionStorage.setItem('settings', JSON.stringify(settings));
    this.autoSelectServiceDefinition(false);
  }

  addPractitionerToStore(practitioner: Practitioner) {
    var settings: Settings = this.sessionStorage['settings'];
    settings.practitioner = practitioner;
    this.sessionStorage.setItem('settings', JSON.stringify(settings));
  }

  addPatientToStore(patient: Patient) {
    this.selectedPatient = patient;
    this.store.dispatch(new PatientActions.AddPatient(patient));
    this.sessionStorage.setItem('patient', JSON.stringify(patient));
    this.autoSelectServiceDefinition(false);
  }

  changeSelectionMode(mode: any) {
    this.serviceDefinitionMode = mode.id;
    if (this.serviceDefinitionMode === 'automated') {
      this.autoSelectServiceDefinition(false);
    } else if (this.cdssSuppliers.length > 0) {
      this.setSelectedSupplier(this.cdssSuppliers[0])
    }
  }

  async autoSelectServiceDefinition(force: boolean) {
    if (this.serviceDefinitionMode === 'automated' || force) {
      this.serviceDefinitionMode = 'automated';

      // Request available SDs for the current patient (or no known patient)
      const request = new SelectService();
      if (this.selectedPatient) {
        request.patientId = this.selectedPatient.id;
      }
      var settings: Settings = this.sessionStorage['settings'];
      request.settings = settings;

      const selectedSDs = await this.triageService.selectServiceDefinitions(request);

      if (selectedSDs.length > 0) {
        this.availableServiceDefinitions = selectedSDs;
      } else {
        this.toastr.info('No available service definitions');
        this.availableServiceDefinitions = [];
      }
    }
  }

  selectServiceDefinition(serviceDefinition: ServiceDefinition) {
    this.selectedServiceDefinition = serviceDefinition.serviceDefinitionId;
  }

  // NCTH-269: Unsure which field should be displayed, for now falling back to ID
  getServiceDefinitionText(serviceDefinition: ServiceDefinition) {
    return serviceDefinition.description || serviceDefinition.serviceDefinitionId;
  }
}
