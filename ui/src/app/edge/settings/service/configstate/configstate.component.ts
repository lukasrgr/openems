import { Component, Input } from '@angular/core';
import { Edge, EdgeConfig, Service, ChannelAddress, Websocket } from '../../../../shared/shared';
import { ModalController } from '@ionic/angular';
import { Subject, BehaviorSubject } from 'rxjs';
import { takeUntil } from 'rxjs/operators';
import { ActivatedRoute } from '@angular/router';
import { isNumber, isUndefined } from 'util';

@Component({
  selector: ConfigStateComponent.SELECTOR,
  templateUrl: './configstate.component.html'
})
export class ConfigStateComponent {

  public loading = true;
  public edge: Edge = null;
  private stopOnDestroy: Subject<void> = new Subject<void>();

  private static readonly SELECTOR = "configState";

  public subscribedChannels: ChannelAddress[] = [];
  public components: EdgeConfig.Component[] = [];
  public appWorking: BehaviorSubject<boolean> = new BehaviorSubject(false);

  constructor(
    private service: Service,
    public modalCtrl: ModalController,
    private websocket: Websocket,
  ) { }

  ionViewDidEnter() {
    this.service.getCurrentEdge().then(edge => {
      this.edge = edge;
    })
    this.gatherAddedComponentsChannels();
    this.edge.currentData.pipe(takeUntil(this.stopOnDestroy)).subscribe(currentData => {
      let workState = 0;
      let errorState = 0;
      if (this.components.length == 3) {
        this.components.forEach(component => {
          let state = currentData.channel[component.id + '/State'];
          if (!isUndefined(state)) {
            if (state == 0) {
              workState += 1;
            }
            if (state > 0) {
              errorState += 1;
            }
          }
        })
        if (workState == 3) {
          this.loading = false;
          this.appWorking.next(true);
        }
        if (errorState > 0) {
          this.loading = false;
          this.appWorking.next(false);
        }
      }
    })
  }

  private gatherAddedComponentsChannels() {
    this.service.getConfig().then(config => {
      config.getComponentsByFactory('Bridge.Modbus.Serial').forEach(component => {
        if (component.id == 'modbus10') {
          this.components.push(component)
        }
      })
      config.getComponentsByFactory('IO.KMtronic').forEach(component => {
        if (component.properties['modbus.id'] == 'modbus10') {
          this.components.push(component)
        }
      })
      config.getComponentsByFactory('Controller.IO.HeatingElement').forEach(component => {
        this.components.push(component)
      })
      this.components.forEach(component => {
        this.subscribedChannels.push(
          new ChannelAddress(component.id, 'State')
        )
      })
      this.components.forEach(component => {
        Object.keys(component.channels).forEach(channel => {
          if (component.channels[channel]['level']) {
            this.subscribedChannels.push(new ChannelAddress(component.id, channel))
          }
        });
      })
      this.edge.subscribeChannels(this.websocket, 'configState', this.subscribedChannels)
    })
  }

  ionViewDidLeave() {
    this.edge.unsubscribeChannels(this.websocket, 'configState');
    this.stopOnDestroy.next();
    this.stopOnDestroy.complete();
  }
}