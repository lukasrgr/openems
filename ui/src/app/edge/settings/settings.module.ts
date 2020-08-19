import { AliasUpdateComponent } from './profile/aliasupdate.component';
import { ChannelsComponent } from './channels/channels.component';
import { ComponentInstallComponent } from './component/install/install.component';
import { ComponentUpdateComponent } from './component/update/update.component';
import { EVCSComponent } from './service/evcs/evcs.component';
import { HeatingElementRTUComponent } from './service/heatingelementrtu/heatingelementrtu.component';
import { HeatingElementTCPComponent } from './service/heatingelementtcp/heatingelementtcp.component';
import { IndexComponent as ComponentInstallIndexComponent } from './component/install/index.component';
import { IndexComponent as ComponentUpdateIndexComponent } from './component/update/index.component';
import { NetworkComponent } from './network/network.component';
import { NgModule } from '@angular/core';
import { ProfileComponent } from './profile/profile.component';
import { ServiceComponent } from './service/service.component';
import { SettingsComponent } from './settings.component';
import { SharedModule } from './../../shared/shared.module';
import { SystemExecuteComponent } from './systemexecute/systemexecute.component';
import { HeatingpumpTCPComponent } from './service/heatingpumptcp/heatingpumptcp.component';

@NgModule({
  imports: [
    SharedModule
  ],
  declarations: [
    AliasUpdateComponent,
    ChannelsComponent,
    ComponentInstallComponent,
    ComponentInstallIndexComponent,
    ComponentUpdateComponent,
    ComponentUpdateIndexComponent,
    EVCSComponent,
    HeatingElementRTUComponent,
    HeatingElementTCPComponent,
    HeatingpumpTCPComponent,
    NetworkComponent,
    ProfileComponent,
    ServiceComponent,
    SettingsComponent,
    SystemExecuteComponent,
  ],
  entryComponents: [
    EVCSComponent,
    HeatingElementRTUComponent,
    HeatingElementTCPComponent,
    HeatingpumpTCPComponent,
  ]
})
export class SettingsModule { }
