import { AdministrationComponent } from './evcs/modal/administration/administration.component';
import { AsymmetricPeakshavingComponent } from './peakshaving/asymmetric/asymmetricpeakshaving.component';
import { AsymmetricPeakshavingModalComponent } from './peakshaving/asymmetric/modal/modal.component';
import { AutarchyComponent } from './autarchy/autarchy.component';
import { LukasComponent } from './lukas/lukas.component';
import { LukasModalComponent } from './lukas/modal/modal.component';
import { AutarchyModalComponent } from './autarchy/modal/modal.component';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { BrowserModule } from '@angular/platform-browser';
import { ChannelthresholdComponent } from './channelthreshold/channelthreshold.component';
import { ChpSocComponent } from './chpsoc/chpsoc.component';
import { ChpsocModalComponent } from './chpsoc/modal/modal.component';
import { ConsumptionComponent } from './consumption/consumption.component';
import { ConsumptionModalComponent } from './consumption/modal/modal.component';
import { DelayedSellToGridComponent } from './delayedselltogrid/delayedselltogrid.component';
import { DelayedSellToGridModalComponent } from './delayedselltogrid/modal/modal.component';
import { DigitalInputComponent } from './digitalinput/digitalinput.component';
import { DigitalInputModalComponent } from './digitalinput/modal/modal.component';
import { EnergymonitorModule } from './energymonitor/energymonitor.module';
import { EvcsChart } from './evcsCluster/modal/evcs-chart/evcs.chart';
import { EvcsClusterComponent } from './evcsCluster/evcsCluster.component';
import { EvcsComponent } from './evcs/evcs.component';
import { EvcsModalComponent } from './evcs/modal/modal.page';
import { EvcsPopoverComponent } from './evcs/modal/popover/popover.page';
import { FixActivePowerComponent } from './fixactivepower/fixactivepower.component';
import { FixActivePowerModalComponent } from './fixactivepower/modal/modal.component';
import { FixDigitalOutputComponent } from './fixdigitaloutput/fixdigitaloutput.component';
import { FixDigitalOutputModalComponent } from './fixdigitaloutput/modal/modal.component';
import { GridComponent } from './grid/grid.component';
import { GridModalComponent } from './grid/modal/modal.component';
import { HeatingElementComponent } from './heatingelement/heatingelement.component';
import { HeatingElementModalComponent } from './heatingelement/modal/modal.component';
import { HeatPumpComponent } from './heatpump/heatpump.component';
import { HeatPumpModalComponent } from './heatpump/modal/modal.component';
import { InfoComponent } from './info/info.component';
import { LiveComponent } from './live.component';
import { ModalComponentEvcsCluster } from './evcsCluster/modal/evcsCluster-modal.page';
import { NgModule } from '@angular/core';
import { OfflineComponent } from './offline/offline.component';
import { ProductionComponent } from './production/production.component';
import { ProductionModalComponent } from './production/modal/modal.component';
import { SelfConsumptionComponent } from './selfconsumption/selfconsumption.component';
import { SelfconsumptionModalComponent } from './selfconsumption/modal/modal.component';
import { SharedModule } from './../../shared/shared.module';
import { SinglethresholdComponent } from './singlethreshold/singlethreshold.component';
import { SinglethresholdModalComponent } from './singlethreshold/modal/modal.component';
import { StorageComponent } from './storage/storage.component';
import { StorageModalComponent } from './storage/modal/modal.component';
import { SymmetricPeakshavingComponent } from './peakshaving/symmetric/symmetricpeakshaving.component';
import { SymmetricPeakshavingModalComponent } from './peakshaving/symmetric/modal/modal.component';
import { TimeslotPeakshavingComponent } from './peakshaving/timeslot/timeslotpeakshaving.component';
import { TimeslotPeakshavingModalComponent } from './peakshaving/timeslot/modal/modal.component';

@NgModule({
  imports: [
    BrowserAnimationsModule,
    BrowserModule,
    EnergymonitorModule,
    SharedModule,
  ],
  entryComponents: [
    AdministrationComponent,
    AsymmetricPeakshavingModalComponent,
    AutarchyModalComponent,
    LukasModalComponent,
    ChpsocModalComponent,
    ConsumptionModalComponent,
    DelayedSellToGridModalComponent,
    DigitalInputComponent,
    DigitalInputModalComponent,
    EvcsModalComponent,
    EvcsPopoverComponent,
    FixActivePowerModalComponent,
    FixDigitalOutputModalComponent,
    GridModalComponent,
    HeatingElementModalComponent,
    HeatPumpModalComponent,
    ModalComponentEvcsCluster,
    ProductionModalComponent,
    SelfconsumptionModalComponent,
    SinglethresholdModalComponent,
    StorageModalComponent,
    SymmetricPeakshavingModalComponent,
    TimeslotPeakshavingModalComponent,
  ],
  declarations: [
    AdministrationComponent,
    AsymmetricPeakshavingComponent,
    AsymmetricPeakshavingModalComponent,
    AutarchyComponent,
    AutarchyModalComponent,
    LukasComponent,
    LukasModalComponent,
    ChannelthresholdComponent,
    ChpSocComponent,
    ChpsocModalComponent,
    ConsumptionComponent,
    ConsumptionModalComponent,
    DelayedSellToGridComponent,
    DelayedSellToGridModalComponent,
    DigitalInputComponent,
    DigitalInputModalComponent,
    EvcsChart,
    EvcsClusterComponent,
    EvcsComponent,
    EvcsModalComponent,
    EvcsPopoverComponent,
    FixActivePowerComponent,
    FixActivePowerModalComponent,
    FixDigitalOutputComponent,
    FixDigitalOutputModalComponent,
    FixDigitalOutputModalComponent,
    GridComponent,
    GridModalComponent,
    HeatingElementComponent,
    HeatingElementModalComponent,
    HeatPumpComponent,
    HeatPumpModalComponent,
    InfoComponent,
    LiveComponent,
    ModalComponentEvcsCluster,
    OfflineComponent,
    ProductionComponent,
    ProductionModalComponent,
    SelfConsumptionComponent,
    SelfconsumptionModalComponent,
    SinglethresholdComponent,
    SinglethresholdModalComponent,
    StorageComponent,
    StorageModalComponent,
    SymmetricPeakshavingComponent,
    SymmetricPeakshavingModalComponent,
    TimeslotPeakshavingComponent,
    TimeslotPeakshavingModalComponent,
  ]
})
export class LiveModule { }
