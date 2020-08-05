import { Component } from '@angular/core';
import { PopoverController } from '@ionic/angular';
import { TranslateService } from '@ngx-translate/core';
import { Service } from 'src/app/shared/shared';

@Component({
    selector: 'service-popover',
    templateUrl: './popover.page.html'
})
export class ServicePopoverComponent {

    constructor(
        public translate: TranslateService,
        public popoverController: PopoverController,
        public service: Service,
    ) { }
}