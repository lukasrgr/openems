import { Component } from '@angular/core';
import { environment } from '../../../../../environments';
import { Edge, EdgeConfig, Service, Websocket, Utils } from '../../../../shared/shared';
import { HeatingElementComponent } from 'src/app/edge/live/heatingelement/heatingelement.component';
import { TranslateService } from '@ngx-translate/core';
import { FormlyFieldConfig } from '@ngx-formly/core';
import { FormGroup } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { ModalController } from '@ionic/angular';

@Component({
  selector: InstallComponent.SELECTOR,
  templateUrl: './install.component.html'
})
export class InstallComponent {

  public form = null;

  public typeForm = null;
  public communicationForm = null;
  public appForm = null;

  public model = null;
  public fields: FormlyFieldConfig[] = null;
  public factory: EdgeConfig.Factory = null;
  private factoryId: string = null;

  private static readonly SELECTOR = "service";

  public env = environment;

  public edge: Edge = null;
  public config: EdgeConfig = null;

  constructor(
    private service: Service,
    private translate: TranslateService,
    private websocket: Websocket,
    private route: ActivatedRoute,
    public modalCtrl: ModalController,
  ) { }

  ngOnInit() {
    this.service.setCurrentComponent('', this.route).then(edge => {
      this.edge = edge;
    });
  }

  gatherType() {
    let factoryId = 'IO.KMtronic'
    this.service.getConfig().then(config => {
      this.factoryId = factoryId;
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

          // Set the next free Component-ID as defaultValue
          if (property_id == 'id') {
            let thisMatch = property.defaultValue.match(/^(.*)(\d+)$/);
            if (thisMatch) {
              let thisPrefix = thisMatch[1];
              let highestSuffix = Number.parseInt(thisMatch[2]);
              for (let componentId of Object.keys(config.components)) {
                let componentMatch = componentId.match(/^(.*)(\d+)$/);
                if (componentMatch) {
                  let componentPrefix = componentMatch[1];
                  if (componentPrefix === thisPrefix) {
                    let componentSuffix = Number.parseInt(componentMatch[2]);
                    highestSuffix = Math.max(highestSuffix, componentSuffix + 1);
                  }
                }
              }
              model[property_id] = thisPrefix + highestSuffix;
            }
          }
        }
      }
      this.typeForm = new FormGroup({});
      this.fields = fields;
      this.model = model;
      console.log("TYP-FORM", this.typeForm)
      console.log("FIELDS", fields)
      console.log("MODEL", model)
    })
  }

  submitType() {
    let properties: { name: string, value: any }[] = [];
    for (let controlKey in this.typeForm.controls) {
      let control = this.typeForm.controls[controlKey];
      let property_id = controlKey.replace('_', '.');
      properties.push({ name: property_id, value: control.value });
    }
    console.log("ERSTELLT MIT: factoryID: ", this.factoryId, " properties: ", properties)
  }

  gatherCommunication() {
    let factoryId = 'Bridge.Modbus.Serial';
    this.service.getConfig().then(config => {
      this.factoryId = factoryId;
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

          // Set the next free Component-ID as defaultValue
          if (property_id == 'id') {
            let thisMatch = property.defaultValue.match(/^(.*)(\d+)$/);
            if (thisMatch) {
              let thisPrefix = thisMatch[1];
              let highestSuffix = Number.parseInt(thisMatch[2]);
              for (let componentId of Object.keys(config.components)) {
                let componentMatch = componentId.match(/^(.*)(\d+)$/);
                if (componentMatch) {
                  let componentPrefix = componentMatch[1];
                  if (componentPrefix === thisPrefix) {
                    let componentSuffix = Number.parseInt(componentMatch[2]);
                    highestSuffix = Math.max(highestSuffix, componentSuffix + 1);
                  }
                }
              }
              model[property_id] = thisPrefix + highestSuffix;
            }
          }
        }
      }
      this.communicationForm = new FormGroup({});
      this.fields = fields;
      this.model = model;
    });
    console.log("COMMUNICATION-FORM", this.communicationForm)
    console.log("FIELDS", this.fields)
    console.log("MODEL", this.model)
  }

  submitCommunication() {
    let properties: { name: string, value: any }[] = [];
    for (let controlKey in this.communicationForm.controls) {
      let control = this.form.controls[controlKey];
      let property_id = controlKey.replace('_', '.');
      properties.push({ name: property_id, value: control.value });
    }
    console.log("ERSTELLT MIT: factoryID: ", this.factoryId, " properties: ", properties)
  }

  gatherApp() {
    let factoryId = 'Controller.IO.HeatingElement';
    this.service.getConfig().then(config => {
      this.factoryId = factoryId;
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

          // Set the next free Component-ID as defaultValue
          if (property_id == 'id') {
            let thisMatch = property.defaultValue.match(/^(.*)(\d+)$/);
            if (thisMatch) {
              let thisPrefix = thisMatch[1];
              let highestSuffix = Number.parseInt(thisMatch[2]);
              for (let componentId of Object.keys(config.components)) {
                let componentMatch = componentId.match(/^(.*)(\d+)$/);
                if (componentMatch) {
                  let componentPrefix = componentMatch[1];
                  if (componentPrefix === thisPrefix) {
                    let componentSuffix = Number.parseInt(componentMatch[2]);
                    highestSuffix = Math.max(highestSuffix, componentSuffix + 1);
                  }
                }
              }
              model[property_id] = thisPrefix + highestSuffix;
            }
          }
        }
      }
      this.appForm = new FormGroup({});
      this.fields = fields;
      this.model = model;
    });
    console.log("APP-FORM", this.appForm)
    console.log("FIELDS", this.fields)
    console.log("MODEL", this.model)
  }

  submitApp() {
    let properties: { name: string, value: any }[] = [];
    for (let controlKey in this.typeForm.controls) {
      let control = this.appForm.controls[controlKey];
      let property_id = controlKey.replace('_', '.');
      properties.push({ name: property_id, value: control.value });
    }
    console.log("ERSTELLT MIT: factoryID: ", this.factoryId, " properties: ", properties)
  }

  public submit() {
    let properties: { name: string, value: any }[] = [];
    for (let controlKey in this.form.controls) {
      let control = this.form.controls[controlKey];
      let property_id = controlKey.replace('_', '.');
      properties.push({ name: property_id, value: control.value });
    }
    console.log("ERSTELLT MIT: factoryID: ", this.factoryId, " properties: ", properties)
    // this.edge.createComponentConfig(this.websocket, this.factoryId, properties).then(response => {
    //   this.form.markAsPristine();
    //   this.service.toast("Successfully created in instance of " + this.factoryId + ".", 'success');
    // }).catch(reason => {
    //   this.service.toast("Error creating an instance of " + this.factoryId + ":" + reason.error.message, 'danger');
    // });
  }
}