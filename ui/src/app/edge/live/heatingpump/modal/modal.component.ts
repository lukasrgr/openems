import { Component, ChangeDetectorRef } from '@angular/core';
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
  public pumpMode: PumpMode = 'Einschaltbefehl';

  public einschaltbefehl: boolean = false;
  public einschaltempfehlung: boolean = false;
  public normalbetrieb: boolean = true;
  public sperre: boolean = false;



  constructor(
    public modalCtrl: ModalController,
    public service: Service,
    private cdr: ChangeDetectorRef
  ) { }

  ngOnInit() {
  }

  public updateControllerMode(event: CustomEvent) {
    let oldMode = this.controllerMode;
    let newMode = event.detail.value;

    if (oldMode != newMode) {
      console.log("anderer mode!", newMode)
      this.controllerMode = newMode;
    }
  }

  public updatePumpMode(event, mode: PumpMode) {
    if (event.detail['checked'] == true) {
      this.pumpMode = mode;
      this.cdr.detectChanges();
    }
    console.log("modeÜbergabe", mode)
    console.log("pumpMode", this.pumpMode)
  }
}