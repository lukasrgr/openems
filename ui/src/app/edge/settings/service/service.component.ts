import { Component } from '@angular/core';
import { environment } from '../../../../environments';
import { Edge, EdgeConfig } from '../../../shared/shared';
import { ModalController } from '@ionic/angular';
import { InstallComponent } from './install/install.component';

@Component({
  selector: ServiceComponent.SELECTOR,
  templateUrl: './service.component.html'
})
export class ServiceComponent {

  private static readonly SELECTOR = "service";

  public env = environment;

  public edge: Edge = null;
  public config: EdgeConfig = null;

  constructor(
    public modalCtrl: ModalController,
  ) { }

  ngOnInit() {
  }

  async presentModal() {
    const modal = await this.modalCtrl.create({
      component: InstallComponent,
    });
    return await modal.present();
  }
}