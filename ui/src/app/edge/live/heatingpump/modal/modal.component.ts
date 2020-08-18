import { Component } from '@angular/core';
import { ModalController } from '@ionic/angular';
import { Service } from '../../../../shared/shared';

@Component({
  selector: HeatingpumpModalComponent.SELECTOR,
  templateUrl: './modal.component.html'
})
export class HeatingpumpModalComponent {

  private static readonly SELECTOR = "heatingpump-modal";

  constructor(
    public modalCtrl: ModalController,
    public service: Service,
  ) { }
}