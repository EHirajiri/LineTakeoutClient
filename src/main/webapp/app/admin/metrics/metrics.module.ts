import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';
import { LineTakeoutClientSharedModule } from 'app/shared/shared.module';

import { MetricsComponent } from './metrics.component';

import { metricsRoute } from './metrics.route';

@NgModule({
  imports: [LineTakeoutClientSharedModule, RouterModule.forChild([metricsRoute])],
  declarations: [MetricsComponent],
})
export class MetricsModule {}
