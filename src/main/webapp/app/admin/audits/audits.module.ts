import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';
import { LineTakeoutClientSharedModule } from 'app/shared/shared.module';

import { AuditsComponent } from './audits.component';

import { auditsRoute } from './audits.route';

@NgModule({
  imports: [LineTakeoutClientSharedModule, RouterModule.forChild([auditsRoute])],
  declarations: [AuditsComponent],
})
export class AuditsModule {}
