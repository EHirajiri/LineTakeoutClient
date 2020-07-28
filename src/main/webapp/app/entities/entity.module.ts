import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

@NgModule({
  imports: [
    RouterModule.forChild([
      {
        path: 'item',
        loadChildren: () => import('./item/item.module').then(m => m.LineTakeoutClientItemModule),
      },
      {
        path: 'customer',
        loadChildren: () => import('./customer/customer.module').then(m => m.LineTakeoutClientCustomerModule),
      },
      {
        path: 'ordered',
        loadChildren: () => import('./ordered/ordered.module').then(m => m.LineTakeoutClientOrderedModule),
      },
      {
        path: 'information',
        loadChildren: () => import('./information/information.module').then(m => m.LineTakeoutClientInformationModule),
      },
      /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
    ]),
  ],
})
export class LineTakeoutClientEntityModule {}
