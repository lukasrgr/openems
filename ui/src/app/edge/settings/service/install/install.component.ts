import { Component, Input } from '@angular/core';
import { environment } from '../../../../../environments';
import { Edge, EdgeConfig, Service, ChannelAddress, Websocket, Utils } from '../../../../shared/shared';
import { FormlyFieldConfig } from '@ngx-formly/core';
import { ModalController } from '@ionic/angular';
import { ActivatedRoute } from '@angular/router';

@Component({
  selector: InstallComponent.SELECTOR,
  templateUrl: './install.component.html'
})
export class InstallComponent {

  @Input() public edge: Edge;
  public config: EdgeConfig = null;

  public factory: EdgeConfig.Factory = null;


  private static readonly SELECTOR = "serviceState";

  public subscribedChannels: ChannelAddress[] = [];
  public components: EdgeConfig.Component[] = [];



  constructor(
    private route: ActivatedRoute,
    private service: Service,
    public modalCtrl: ModalController,
    private websocket: Websocket,
  ) { }

  // used to assemble properties out of created fields and model from gather methods
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
    this.factory = config.factories[factoryId];
    let fields: FormlyFieldConfig[] = [];
    let model = {};
    for (let property of this.factory.properties) {
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
        // sets costum modbus-id
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
    this.factory = config.factories[factoryId];
    let fields: FormlyFieldConfig[] = [];
    let model = {};
    for (let property of this.factory.properties) {
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
    this.factory = config.factories[factoryId];
    let fields: FormlyFieldConfig[] = [];
    let model = {};
    for (let property of this.factory.properties) {
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
        // sets costum component id
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
        }).catch(reason => {
          this.service.toast("Error creating an instance of " + 'Controller.IO.HeatingElement' + ":" + reason.error.message, 'danger');
        })
      }).catch(reason => {
        this.service.toast("Error creating an instance of " + 'IO.KMtronic' + ":" + reason.error.message, 'danger');
      })
    }).catch(reason => {
      this.service.toast("Error creating an instance of " + 'Bridge.Modbus.Serial' + ":" + reason.error.message, 'danger');
    });
  }

  ionViewDidEnter() {
    this.service.getConfig().then(config => {
      this.config = config;
    });
  }

  gatherAddedComponentsChannels() {
    this.config.getComponentsByFactory('Bridge.Modbus.Serial').forEach(component => {
      if (component.id == 'modbus10') {
        this.components.push(component)
      }
    })
    this.config.getComponentsByFactory('IO.KMtronic').forEach(component => {
      if (component.properties['modbus.id'] == 'modbus10') {
        this.components.push(component)
      }
    })
    this.config.getComponentsByFactory('Controller.IO.HeatingElement').forEach(component => {
      this.components.push(component)
    })
    this.components.forEach(component => {
      this.subscribedChannels.push(
        new ChannelAddress(component.id, 'State')
      )
    })
    this.edge.subscribeChannels(this.websocket, 'serviceState', this.subscribedChannels)
  }

  ionViewDidLeave() {
    this.edge.unsubscribeChannels(this.websocket, 'serviceState')
  }
}