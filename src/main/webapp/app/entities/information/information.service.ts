import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import * as moment from 'moment';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared/util/request-util';
import { IInformation } from 'app/shared/model/information.model';

type EntityResponseType = HttpResponse<IInformation>;
type EntityArrayResponseType = HttpResponse<IInformation[]>;

@Injectable({ providedIn: 'root' })
export class InformationService {
  public resourceUrl = SERVER_API_URL + 'api/information';

  constructor(protected http: HttpClient) {}

  create(information: IInformation): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(information);
    return this.http
      .post<IInformation>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(information: IInformation): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(information);
    return this.http
      .put<IInformation>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<IInformation>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IInformation[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  protected convertDateFromClient(information: IInformation): IInformation {
    const copy: IInformation = Object.assign({}, information, {
      createdDate: information.createdDate && information.createdDate.isValid() ? information.createdDate.toJSON() : undefined,
      lastModifiedDate:
        information.lastModifiedDate && information.lastModifiedDate.isValid() ? information.lastModifiedDate.toJSON() : undefined,
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
      res.body.forEach((information: IInformation) => {
        information.createdDate = information.createdDate ? moment(information.createdDate) : undefined;
        information.lastModifiedDate = information.lastModifiedDate ? moment(information.lastModifiedDate) : undefined;
      });
    }
    return res;
  }
}
