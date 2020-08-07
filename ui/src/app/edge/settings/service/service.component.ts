import { ActivatedRoute } from '@angular/router';
import { Component } from '@angular/core';
import { ConfigStateComponent } from './configstate/configstate.component';
import { Edge, EdgeConfig, Service, Utils, Websocket } from '../../../shared/shared';
import { ModalController, PopoverController } from '@ionic/angular';
import { TranslateService } from '@ngx-translate/core';

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
    private translate: TranslateService,
  ) { }

  ngOnInit() {
    this.service.setCurrentComponent(this.translate.instant('Edge.Config.Index.service'), this.route).then(edge => {
      this.edge = edge;
    });
    this.service.getConfig().then(config => {
      this.config = config;
    });
  }

  async presentModal() {
    const modal = await this.modalCtrl.create({
      component: ConfigStateComponent,
      componentProps: {
        edge: this.edge,
      }
    });
    modal.onDidDismiss().then(() => {
      this.loading = false;
    })
    return await modal.present();
  }
}