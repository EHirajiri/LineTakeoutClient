import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { IOrdered } from 'app/shared/model/ordered.model';
import { OrderedService } from './ordered.service';
import { DeliveryState } from 'app/shared/model/enumerations/delivery-state.model';

@Component({
  selector: 'jhi-ordered-detail',
  templateUrl: './ordered-detail.component.html',
})
export class OrderedDetailComponent implements OnInit {
  ordered: IOrdered | null = null;

  constructor(protected activatedRoute: ActivatedRoute, protected orderedService: OrderedService) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ ordered }) => (this.ordered = ordered));
  }

  previousState(): void {
    window.history.back();
  }

  accept(ordered: IOrdered): void {
    ordered.deliveryState = DeliveryState.ACCEPT;
    this.subscribeToSaveResponse(this.orderedService.accept(ordered));
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IOrdered>>): void {
    result.subscribe();
  }
}
