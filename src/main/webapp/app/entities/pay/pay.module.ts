import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { LineTakeoutClientSharedModule } from 'app/shared/shared.module';
import { PayComponent } from './pay.component';
import { PayDetailComponent } from './pay-detail.component';
import { PayUpdateComponent } from './pay-update.component';
import { PayDeleteDialogComponent } from './pay-delete-dialog.component';
import { payRoute } from './pay.route';

@NgModule({
  imports: [LineTakeoutClientSharedModule, RouterModule.forChild(payRoute)],
  declarations: [PayComponent, PayDetailComponent, PayUpdateComponent, PayDeleteDialogComponent],
  entryComponents: [PayDeleteDialogComponent],
})
export class LineTakeoutClientPayModule {}
