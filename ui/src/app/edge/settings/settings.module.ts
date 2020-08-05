import { AliasUpdateComponent } from './profile/aliasupdate.component';
import { ChannelsComponent } from './channels/channels.component';
import { ComponentInstallComponent } from './component/install/install.component';
import { ComponentUpdateComponent } from './component/update/update.component';
import { ConfigStateComponent } from './service/configstate/configstate.component';
import { IndexComponent as ComponentInstallIndexComponent } from './component/install/index.component';
import { IndexComponent as ComponentUpdateIndexComponent } from './component/update/index.component';
import { NetworkComponent } from './network/network.component';
import { NgModule } from '@angular/core';
import { ProfileComponent } from './profile/profile.component';
import { ServiceComponent } from './service/service.component';
import { SettingsComponent } from './settings.component';
import { SharedModule } from './../../shared/shared.module';
import { SystemExecuteComponent } from './systemexecute/systemexecute.component';
import { ServicePopoverComponent } from './service/popover/popover.page';

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
    ConfigStateComponent,
    NetworkComponent,
    ProfileComponent,
    ServiceComponent,
    ServicePopoverComponent,
    SettingsComponent,
    SystemExecuteComponent,
  ],
  entryComponents: [
    ConfigStateComponent,
    ServicePopoverComponent,
  ]
})
export class SettingsModule { }
