import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Routes, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { flatMap } from 'rxjs/operators';

import { Authority } from 'app/shared/constants/authority.constants';
import { UserRouteAccessService } from 'app/core/auth/user-route-access-service';
import { IInformation, Information } from 'app/shared/model/information.model';
import { InformationService } from './information.service';
import { InformationComponent } from './information.component';
import { InformationDetailComponent } from './information-detail.component';
import { InformationUpdateComponent } from './information-update.component';

@Injectable({ providedIn: 'root' })
export class InformationResolve implements Resolve<IInformation> {
  constructor(private service: InformationService, private router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IInformation> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        flatMap((information: HttpResponse<Information>) => {
          if (information.body) {
            return of(information.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new Information());
  }
}

export const informationRoute: Routes = [
  {
    path: '',
    component: InformationComponent,
    data: {
      authorities: [Authority.USER],
      defaultSort: 'id,asc',
      pageTitle: 'lineTakeoutClientApp.information.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: InformationDetailComponent,
    resolve: {
      information: InformationResolve,
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'lineTakeoutClientApp.information.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: InformationUpdateComponent,
    resolve: {
      information: InformationResolve,
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'lineTakeoutClientApp.information.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: InformationUpdateComponent,
    resolve: {
      information: InformationResolve,
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'lineTakeoutClientApp.information.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
];
