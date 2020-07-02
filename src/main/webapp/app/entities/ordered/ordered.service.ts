import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared/util/request-util';
import { IOrdered } from 'app/shared/model/ordered.model';

type EntityResponseType = HttpResponse<IOrdered>;
type EntityArrayResponseType = HttpResponse<IOrdered[]>;

@Injectable({ providedIn: 'root' })
export class OrderedService {
  public resourceUrl = SERVER_API_URL + 'api/ordereds';

  constructor(protected http: HttpClient) {}

  create(ordered: IOrdered): Observable<EntityResponseType> {
    return this.http.post<IOrdered>(this.resourceUrl, ordered, { observe: 'response' });
  }

  update(ordered: IOrdered): Observable<EntityResponseType> {
    return this.http.put<IOrdered>(this.resourceUrl, ordered, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IOrdered>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IOrdered[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }
}
