import { Component } from '@angular/core';
import { environment } from '../../../../environments';
import { Edge, EdgeConfig, Service, Utils, Websocket } from '../../../shared/shared';
import { ModalController } from '@ionic/angular';
import { InstallComponent } from './install/install.component';
import { FormlyFieldConfig } from '@ngx-formly/core';
import { FormGroup } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';

@Component({
  selector: ServiceComponent.SELECTOR,
  templateUrl: './service.component.html'
})
export class ServiceComponent {

  private static readonly SELECTOR = "service";

  public loading: boolean = false;

  public edge: Edge = null;
  public config: EdgeConfig = null;

  public factory: EdgeConfig.Factory = null;

  constructor(
    private route: ActivatedRoute,
    private service: Service,
    private websocket: Websocket,
    public modalCtrl: ModalController,
  ) { }

  ngOnInit() {
    this.service.setCurrentComponent('service', this.route).then(edge => {
      this.edge = edge;
    });
    this.service.getConfig().then(config => {
      this.config = config;
    });
  }

  async presentModal() {
    const modal = await this.modalCtrl.create({
      component: InstallComponent,
      componentProps: {
        edge: this.edge,
      }
    });
    return await modal.present();
  }
}