import { Component, Input, Output, EventEmitter } from '@angular/core';
import { Practitioner } from 'src/app/model/practitioner';

@Component({
  selector: 'practitioner-selection',
  templateUrl: './practitioner-selection.component.html',
  styleUrls: ['../main.component.css']
})
export class PractitionerSelectionComponent {

  @Input() practitioners: Practitioner[];
  @Output() onChange = new EventEmitter<Practitioner>();

  selectedPractitioner: Practitioner;

  change(selection: Practitioner) {
    this.onChange.emit(selection);
    this.selectedPractitioner = selection;
  }

}
