import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

@NgModule({
  imports: [
    RouterModule.forChild([
      {
        path: 'customer',
        loadChildren: () => import('./customer/customer.module').then(m => m.LineTakeoutClientCustomerModule),
      },
      {
        path: 'item',
        loadChildren: () => import('./item/item.module').then(m => m.LineTakeoutClientItemModule),
      },
      /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
    ]),
  ],
})
export class LineTakeoutClientEntityModule {}
