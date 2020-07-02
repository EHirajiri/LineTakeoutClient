import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { LineTakeoutClientSharedModule } from 'app/shared/shared.module';
import { OrderedComponent } from './ordered.component';
import { OrderedDetailComponent } from './ordered-detail.component';
import { OrderedUpdateComponent } from './ordered-update.component';
import { OrderedDeleteDialogComponent } from './ordered-delete-dialog.component';
import { orderedRoute } from './ordered.route';

@NgModule({
  imports: [LineTakeoutClientSharedModule, RouterModule.forChild(orderedRoute)],
  declarations: [OrderedComponent, OrderedDetailComponent, OrderedUpdateComponent, OrderedDeleteDialogComponent],
  entryComponents: [OrderedDeleteDialogComponent],
})
export class LineTakeoutClientOrderedModule {}
