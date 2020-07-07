import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import * as moment from 'moment';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared/util/request-util';
import { IPay } from 'app/shared/model/pay.model';

type EntityResponseType = HttpResponse<IPay>;
type EntityArrayResponseType = HttpResponse<IPay[]>;

@Injectable({ providedIn: 'root' })
export class PayService {
  public resourceUrl = SERVER_API_URL + 'api/pays';

  constructor(protected http: HttpClient) {}

  create(pay: IPay): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(pay);
    return this.http
      .post<IPay>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(pay: IPay): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(pay);
    return this.http
      .put<IPay>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<IPay>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IPay[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  protected convertDateFromClient(pay: IPay): IPay {
    const copy: IPay = Object.assign({}, pay, {
      paidDate: pay.paidDate && pay.paidDate.isValid() ? pay.paidDate.toJSON() : undefined,
      receivedDate: pay.receivedDate && pay.receivedDate.isValid() ? pay.receivedDate.toJSON() : undefined,
      createdDate: pay.createdDate && pay.createdDate.isValid() ? pay.createdDate.toJSON() : undefined,
      lastModifiedDate: pay.lastModifiedDate && pay.lastModifiedDate.isValid() ? pay.lastModifiedDate.toJSON() : undefined,
    });
    return copy;
  }

  protected convertDateFromServer(res: EntityResponseType): EntityResponseType {
    if (res.body) {
      res.body.paidDate = res.body.paidDate ? moment(res.body.paidDate) : undefined;
      res.body.receivedDate = res.body.receivedDate ? moment(res.body.receivedDate) : undefined;
      res.body.createdDate = res.body.createdDate ? moment(res.body.createdDate) : undefined;
      res.body.lastModifiedDate = res.body.lastModifiedDate ? moment(res.body.lastModifiedDate) : undefined;
    }
    return res;
  }

  protected convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
    if (res.body) {
      res.body.forEach((pay: IPay) => {
        pay.paidDate = pay.paidDate ? moment(pay.paidDate) : undefined;
        pay.receivedDate = pay.receivedDate ? moment(pay.receivedDate) : undefined;
        pay.createdDate = pay.createdDate ? moment(pay.createdDate) : undefined;
        pay.lastModifiedDate = pay.lastModifiedDate ? moment(pay.lastModifiedDate) : undefined;
      });
    }
    return res;
  }
}
