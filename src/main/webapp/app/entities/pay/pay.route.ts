import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Routes, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { flatMap } from 'rxjs/operators';

import { Authority } from 'app/shared/constants/authority.constants';
import { UserRouteAccessService } from 'app/core/auth/user-route-access-service';
import { IPay, Pay } from 'app/shared/model/pay.model';
import { PayService } from './pay.service';
import { PayComponent } from './pay.component';
import { PayDetailComponent } from './pay-detail.component';
import { PayUpdateComponent } from './pay-update.component';

@Injectable({ providedIn: 'root' })
export class PayResolve implements Resolve<IPay> {
  constructor(private service: PayService, private router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IPay> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        flatMap((pay: HttpResponse<Pay>) => {
          if (pay.body) {
            return of(pay.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new Pay());
  }
}

export const payRoute: Routes = [
  {
    path: '',
    component: PayComponent,
    data: {
      authorities: [Authority.USER],
      defaultSort: 'id,asc',
      pageTitle: 'lineTakeoutClientApp.pay.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: PayDetailComponent,
    resolve: {
      pay: PayResolve,
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'lineTakeoutClientApp.pay.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: PayUpdateComponent,
    resolve: {
      pay: PayResolve,
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'lineTakeoutClientApp.pay.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: PayUpdateComponent,
    resolve: {
      pay: PayResolve,
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'lineTakeoutClientApp.pay.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
];
