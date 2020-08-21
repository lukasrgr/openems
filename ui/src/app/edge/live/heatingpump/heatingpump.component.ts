import { ActivatedRoute } from '@angular/router';
import { HeatingpumpModalComponent } from './modal/modal.component';
import { Component } from '@angular/core';
import { Edge, Service } from '../../../shared/shared';
import { ModalController } from '@ionic/angular';

@Component({
  selector: HeatingpumpComponent.SELECTOR,
  templateUrl: './heatingpump.component.html'
})
export class HeatingpumpComponent {

  private static readonly SELECTOR = "heatingpump";

  private edge: Edge = null;

  constructor(
    private route: ActivatedRoute,
    public modalCtrl: ModalController,
    public service: Service,
  ) { }

  ngOnInit() {
    this.service.setCurrentComponent('', this.route).then(edge => {
      this.edge = edge;
    })
  }

  async presentModal() {
    const modal = await this.modalCtrl.create({
      component: HeatingpumpModalComponent,
    });
    return await modal.present();
  }
}
