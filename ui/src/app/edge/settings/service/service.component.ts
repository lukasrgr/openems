import { ActivatedRoute } from '@angular/router';
import { Component } from '@angular/core';
import { ConfigStateComponent } from './configstate/configstate.component';
import { Edge, EdgeConfig, Service, Utils, Websocket } from '../../../shared/shared';
import { FormlyFieldConfig } from '@ngx-formly/core';
import { ModalController, PopoverController } from '@ionic/angular';
import { ServicePopoverComponent } from './popover/popover.page';

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
    public popoverController: PopoverController,
  ) { }

  ngOnInit() {
    this.service.setCurrentComponent('service', this.route).then(edge => {
      this.edge = edge;
    });
    this.service.getConfig().then(config => {
      this.config = config;
    });
  }

  // used to assemble properties out of created fields and model from 'gather' methods
  private createProperties(fields: FormlyFieldConfig[], model): { name: string, value: any }[] {
    let result: { name: string, value: any }[] = [];
    fields.forEach(field => {
      if (field.key == 'alias') {
        result.push({ name: 'alias', value: '' })
      }
      Object.keys(model).forEach(modelKey => {
        if (field.key == modelKey) {
          result.push({ name: field.key.toString(), value: model[modelKey] })
        }
      })
    })
    return result;
  }

  private gatherType(config: EdgeConfig): { name: string, value: any }[] {
    let factoryId = 'IO.KMtronic'
    let factory = config.factories[factoryId];
    let fields: FormlyFieldConfig[] = [];
    let model = {};
    for (let property of factory.properties) {
      let property_id = property.id.replace('.', '_');
      let field: FormlyFieldConfig = {
        key: property_id,
        type: 'input',
        templateOptions: {
          label: property.name,
          description: property.description
        }
      }
      // add Property Schema 
      Utils.deepCopy(property.schema, field);
      fields.push(field);
      if (property.defaultValue != null) {
        model[property_id] = property.defaultValue;
        // set costum modbus-id
        if (property.name == 'Modbus-ID') {
          model[property_id] = 'modbus10';
        }
      }
    }
    let properties = this.createProperties(fields, model);
    return properties;
  }

  private gatherApp(config: EdgeConfig): { name: string, value: any }[] {
    let factoryId = 'Controller.IO.HeatingElement';
    let factory = config.factories[factoryId];
    let fields: FormlyFieldConfig[] = [];
    let model = {};
    for (let property of factory.properties) {
      let property_id = property.id.replace('.', '_');
      let field: FormlyFieldConfig = {
        key: property_id,
        type: 'input',
        templateOptions: {
          label: property.name,
          description: property.description
        }
      }
      // add Property Schema 
      Utils.deepCopy(property.schema, field);
      fields.push(field);
      if (property.defaultValue != null) {
        model[property_id] = property.defaultValue;
      }
    }
    let properties = this.createProperties(fields, model);
    return properties;
  }

  private gatherCommunication(config: EdgeConfig): { name: string, value: any }[] {
    let factoryId = 'Bridge.Modbus.Serial';
    let factory = config.factories[factoryId];
    let fields: FormlyFieldConfig[] = [];
    let model = {};
    for (let property of factory.properties) {
      let property_id = property.id.replace('.', '_');
      let field: FormlyFieldConfig = {
        key: property_id,
        type: 'input',
        templateOptions: {
          label: property.name,
          description: property.description
        }
      }
      // add Property Schema 
      Utils.deepCopy(property.schema, field);
      fields.push(field);
      if (property.defaultValue != null) {
        model[property_id] = property.defaultValue;
        // set costum component id
        if (property.name == 'Component-ID') {
          model[property_id] = 'modbus10';
        }
      }
    }
    let properties = this.createProperties(fields, model);
    return properties;
  }

  addHeatingElement() {
    this.edge.createComponentConfig(this.websocket, 'Bridge.Modbus.Serial', this.gatherCommunication(this.config)).then(() => {
      this.edge.createComponentConfig(this.websocket, 'IO.KMtronic', this.gatherType(this.config)).then(() => {
        this.edge.createComponentConfig(this.websocket, 'Controller.IO.HeatingElement', this.gatherApp(this.config)).then(() => {
          this.service.toast("Heizstab APP erfolgreich hinzugefügt", 'success');
          this.presentModal();
        }).catch(reason => {
          if (reason.error.code == 1) {
            this.service.toast("Heizstab existiert bereits!", 'danger');
            return;
          }
          this.service.toast("Error creating an instance of " + 'Controller.IO.HeatingElement' + ":" + reason.error.message, 'danger');
        })
      }).catch(reason => {
        if (reason.error.code == 1) {
          this.service.toast("Heizstab existiert bereits!", 'danger');
          return;
        }
        this.service.toast("Error creating an instance of " + 'IO.KMtronic' + ":" + reason.error.message, 'danger');
      })
    }).catch(reason => {
      if (reason.error.code == 1) {
        this.service.toast("Heizstab existiert bereits!", 'danger');
        return;
      }
      this.service.toast("Error creating an instance of " + 'Bridge.Modbus.Serial' + ":" + reason.error.message, 'danger');
    });
  }

  async presentPopover(ev: any) {
    const popover = await this.popoverController.create({
      component: ServicePopoverComponent,
      event: ev,
      translucent: true
    });
    popover.onDidDismiss().then((data) => {
      if (data.data == true) {
        this.addHeatingElement()
      }
    })
    return await popover.present();
  }

  async presentModal() {
    const modal = await this.modalCtrl.create({
      component: ConfigStateComponent,
      componentProps: {
        edge: this.edge,
      }
    });
    return await modal.present();
  }
}