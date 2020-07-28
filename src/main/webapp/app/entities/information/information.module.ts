import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { LineTakeoutClientSharedModule } from 'app/shared/shared.module';
import { InformationComponent } from './information.component';
import { InformationDetailComponent } from './information-detail.component';
import { InformationUpdateComponent } from './information-update.component';
import { InformationDeleteDialogComponent } from './information-delete-dialog.component';
import { informationRoute } from './information.route';

@NgModule({
  imports: [LineTakeoutClientSharedModule, RouterModule.forChild(informationRoute)],
  declarations: [InformationComponent, InformationDetailComponent, InformationUpdateComponent, InformationDeleteDialogComponent],
  entryComponents: [InformationDeleteDialogComponent],
})
export class LineTakeoutClientInformationModule {}
