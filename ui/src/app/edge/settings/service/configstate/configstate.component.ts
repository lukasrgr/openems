import { Component, Input } from '@angular/core';
import { Edge, EdgeConfig, Service, ChannelAddress, Websocket, Utils } from '../../../../shared/shared';
import { ModalController } from '@ionic/angular';
import { Subject, BehaviorSubject } from 'rxjs';
import { takeUntil } from 'rxjs/operators';
import { ActivatedRoute } from '@angular/router';
import { isNumber, isUndefined } from 'util';
import { FormlyFieldConfig } from '@ngx-formly/core';
import { TranslateService } from '@ngx-translate/core';

@Component({
  selector: ConfigStateComponent.SELECTOR,
  templateUrl: './configstate.component.html'
})
export class ConfigStateComponent {

  public loading = true;
  public running = false;
  public showInit: boolean = false;
  public loadingStrings = [];

  public edge: Edge = null;
  public config: EdgeConfig = null;
  private stopOnDestroy: Subject<void> = new Subject<void>();

  private static readonly SELECTOR = "configState";

  public subscribedChannels: ChannelAddress[] = [];
  public components: EdgeConfig.Component[] = [];
  public appWorking: BehaviorSubject<boolean> = new BehaviorSubject(false);

  constructor(
    public service: Service,
    public modalCtrl: ModalController,
    private websocket: Websocket,
    private translate: TranslateService,
  ) { }

  ngOnInit() {
    this.service.getConfig().then(config => {
      this.config = config;
    }).then(() => {
      switch (this.gatherAddedComponents().length) {
        case 0: {
          this.showInit = true;
          this.loading = false;
          break;
        }
        case 1: {
          break;
        }
        case 2: {
          break;
        }
        case 3: {
          this.loading = false;
          this.running = true;
          break;
        }
      }
    });
    this.service.getCurrentEdge().then(edge => {
      this.edge = edge;
    })
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
        if (property.name == 'Mode') {
          model[property_id] = 'MANUAL_OFF';
        }
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

  addHeatingElementComponents() {
    this.loading = true;
    this.showInit = false;
    this.loadingStrings.push('Versuche IO.KMtronic hinzuzufügen..')
    this.edge.createComponentConfig(this.websocket, 'IO.KMtronic', this.gatherType(this.config)).then(response => {
      this.loadingStrings.push('IO.KMtronic erfolgreich hinzugefügt')
    }).catch(reason => {
      if (reason.error.code == 1) {
        this.loadingStrings.push('IO.KMtronic existiert bereits!');
        return;
      }
      this.loadingStrings.push('Fehler IO.KMtronic hinzuzufügen!');
    });
    setTimeout(() => {
      this.loadingStrings.push('Versuche Bridge.Modbus.Serial hinzuzufügen..')
      this.edge.createComponentConfig(this.websocket, 'Bridge.Modbus.Serial', this.gatherCommunication(this.config)).then(response => {
        this.loadingStrings.push('Bridge.Modbus.Serial erfolgreich hinzugefügt')
      }).catch(reason => {
        if (reason.error.code == 1) {
          this.loadingStrings.push('Bridge.Modbus.Serial existiert bereits!');
          return;
        }
        this.loadingStrings.push('Fehler Bridge.Modbus.Serial hinzuzufügen!');
      });
    }, 1000);

    setTimeout(() => {
      this.loadingStrings.push('Versuche Controller.IO.HeatingElement hinzuzufügen..')
      this.edge.createComponentConfig(this.websocket, 'Controller.IO.HeatingElement', this.gatherApp(this.config)).then(response => {
        this.loadingStrings.push('Controller.IO.HeatingElement erfolgreich hinzugefügt')
      }).catch(reason => {
        if (reason.error.code == 1) {
          this.loadingStrings.push('Controller.IO.HeatingElement existiert bereits!');
          return;
        }
        this.loadingStrings.push('Fehler Controller.IO.HeatingElement hinzuzufügen!');
      });
    }, 2000);


    setTimeout(() => {
      this.loadingStrings.push('Überprüfe ob Komponenten korrekt hinzugefügt wurden..');
    }, 4000);




    setTimeout(() => {
      this.checkConfiguration();
    }, 8000);
  }

  public gatherAddedComponents(): EdgeConfig.Component[] {
    let result = [];
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
    return result
  }

  private checkConfiguration() {
    this.service.getConfig().then(config => {
      this.config = config;
    })
    setTimeout(() => {
      console.log("length!", this.gatherAddedComponents().length)
      if (this.gatherAddedComponents().length == 3) {
        this.loadingStrings.push('Komponenten korrekt hinzugefügt');
        this.subscribeOnAddedComponents();
        return
      }
      this.subscribeOnAddedComponents();
      this.loadingStrings.push('Es konnten nicht alle Komponenten korrekt hinzugefügt werden');
    }, 5000);
  }

  private subscribeOnAddedComponents() {
    this.loadingStrings.push('Überprüfe Status der Komponenten..');
    this.components.forEach(component => {
      Object.keys(component.channels).forEach(channel => {
        this.subscribedChannels.push(
          new ChannelAddress(component.id, 'State')
        )
        if (component.channels[channel]['level']) {
          this.subscribedChannels.push(new ChannelAddress(component.id, channel))
        }
      });
    })
    this.edge.subscribeChannels(this.websocket, 'configState', this.subscribedChannels);
    setTimeout(() => {
      this.loading = false;
      this.running = true;
    }, 5000);
  }

  ionViewDidLeave() {
    this.edge.unsubscribeChannels(this.websocket, 'configState');
    this.stopOnDestroy.next();
    this.stopOnDestroy.complete();
  }
}