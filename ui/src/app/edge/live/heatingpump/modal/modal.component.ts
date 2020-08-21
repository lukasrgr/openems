import { Component } from '@angular/core';
import { ModalController } from '@ionic/angular';
import { Service } from '../../../../shared/shared';

type ControllerMode = 'Manual' | 'Auto';
type PumpMode = 'Einschaltbefehl' | 'Einschaltempfehlung' | 'Normalbetrieb' | 'Sperre';

@Component({
  selector: HeatingpumpModalComponent.SELECTOR,
  templateUrl: './modal.component.html'
})
export class HeatingpumpModalComponent {

  private static readonly SELECTOR = "heatingpump-modal";

  public controllerMode: ControllerMode = 'Manual';
  public pumpMode: PumpMode = 'Normalbetrieb';
  public pumpModeValue: Number = null;


  constructor(
    public modalCtrl: ModalController,
    public service: Service,
  ) { }

  ngOnInit() {
    switch (this.pumpMode) {
      case 1: {
        //statements; 
        break;
      }
      case 2: {
        //statements; 
        break;
      }
    }
  }

  updateControllerMode(event: CustomEvent) {
    let oldMode = this.controllerMode;
    let newMode = event.detail.value;

    console.log("oldMode", oldMode);
    console.log("newMode", newMode);

    if (oldMode != newMode) {
      console.log("anderer mode!", newMode)
      this.controllerMode = newMode;
    }
  }

  updatePumpMode(event: CustomEvent) {
    let oldMode = this.pumpMode;
    let newMode = event.detail.value;

    console.log("oldMode", oldMode);
    console.log("newMode", newMode);

    if (oldMode != newMode) {
      console.log("anderer mode!", newMode)
      this.pumpMode = newMode;
    }
  }
}