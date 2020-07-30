import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import * as moment from 'moment';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared/util/request-util';
import { IOrderItem } from 'app/shared/model/order-item.model';

type EntityResponseType = HttpResponse<IOrderItem>;
type EntityArrayResponseType = HttpResponse<IOrderItem[]>;

@Injectable({ providedIn: 'root' })
export class OrderItemService {
  public resourceUrl = SERVER_API_URL + 'api/order-items';

  constructor(protected http: HttpClient) {}

  create(orderItem: IOrderItem): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(orderItem);
    return this.http
      .post<IOrderItem>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(orderItem: IOrderItem): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(orderItem);
    return this.http
      .put<IOrderItem>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<IOrderItem>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IOrderItem[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  protected convertDateFromClient(orderItem: IOrderItem): IOrderItem {
    const copy: IOrderItem = Object.assign({}, orderItem, {
      createdDate: orderItem.createdDate && orderItem.createdDate.isValid() ? orderItem.createdDate.toJSON() : undefined,
      lastModifiedDate:
        orderItem.lastModifiedDate && orderItem.lastModifiedDate.isValid() ? orderItem.lastModifiedDate.toJSON() : undefined,
    });
    return copy;
  }

  protected convertDateFromServer(res: EntityResponseType): EntityResponseType {
    if (res.body) {
      res.body.createdDate = res.body.createdDate ? moment(res.body.createdDate) : undefined;
      res.body.lastModifiedDate = res.body.lastModifiedDate ? moment(res.body.lastModifiedDate) : undefined;
    }
    return res;
  }

  protected convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
    if (res.body) {
      res.body.forEach((orderItem: IOrderItem) => {
        orderItem.createdDate = orderItem.createdDate ? moment(orderItem.createdDate) : undefined;
        orderItem.lastModifiedDate = orderItem.lastModifiedDate ? moment(orderItem.lastModifiedDate) : undefined;
      });
    }
    return res;
  }
}
