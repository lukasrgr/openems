import { Component, Input } from '@angular/core';
import { Edge, EdgeConfig, Service, ChannelAddress, Websocket } from '../../../../shared/shared';
import { ModalController } from '@ionic/angular';
import { Subject, BehaviorSubject } from 'rxjs';
import { takeUntil } from 'rxjs/operators';

@Component({
  selector: ConfigStateComponent.SELECTOR,
  templateUrl: './configstate.component.html'
})
export class ConfigStateComponent {

  @Input() public edge: Edge;

  public loading = true;
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
    setTimeout(() => {
      this.loading = false;
    }, 5000);

    this.gatherAddedComponentsChannels();

    this.edge.currentData.pipe(takeUntil(this.stopOnDestroy)).subscribe(currentData => {
      let sumState = 0;
      this.components.forEach(component => {
        let state = currentData.channel[component.id + '/State'];
        if (state >= 0) {
          sumState += state;
        }
      })
      if (sumState == 0) {
        this.appWorking.next(true);
        return
      }
      this.appWorking.next(false);
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