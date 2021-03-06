import {Component, Inject, OnInit} from '@angular/core';
import {MAT_DIALOG_DATA, MatDialogRef} from '@angular/material';
import {DialogData} from '../triage/triage.component';
import {ServiceDefinitionService} from '../service/service-definition.service';
import {ToastrService} from 'ngx-toastr';

@Component({
  selector: 'app-switch-service-prompt-dialog',
  templateUrl: './switch-service-prompt-dialog.component.html',
  styleUrls: ['./switch-service-prompt-dialog.component.css']
})
export class SwitchServicePromptDialogComponent implements OnInit {
  oldServiceDefinition: any;
  newServiceDefinition: any;

  constructor(
      public dialogRef: MatDialogRef<SwitchServicePromptDialogComponent>,
      @Inject(MAT_DIALOG_DATA) public data: DialogData,
      private serviceDefintionService: ServiceDefinitionService,
      private toastr: ToastrService
  ) {
  }

  async ngOnInit() {
    try {
      const CdssUrl = await this.serviceDefintionService.getCdssSupplierUrl(
          this.data.cdssSupplierId
      );
      this.oldServiceDefinition = await this.serviceDefintionService.getServiceDefinition(
          CdssUrl + 'ServiceDefinition/' + this.data.oldServiceDefinition
      ).toPromise();
      this.newServiceDefinition = await this.serviceDefintionService.getServiceDefinition(
          CdssUrl + 'ServiceDefinition/' + this.data.newServiceDefinition
      ).toPromise();
    } catch (err) {
      this.toastr.error(err.error.target.__zone_symbol__xhrURL + ' - ' + err.message);
    }
  }

  onNoClick(): void {
    this.dialogRef.close();
  }
}
