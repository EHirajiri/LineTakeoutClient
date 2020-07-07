import { Moment } from 'moment';
import { IOrdered } from 'app/shared/model/ordered.model';
import { IPay } from 'app/shared/model/pay.model';

export interface ICustomer {
  id?: number;
  userId?: string;
  nickname?: string;
  language?: string;
  createdBy?: string;
  createdDate?: Moment;
  lastModifiedBy?: string;
  lastModifiedDate?: Moment;
  ordereds?: IOrdered[];
  pays?: IPay[];
}

export class Customer implements ICustomer {
  constructor(
    public id?: number,
    public userId?: string,
    public nickname?: string,
    public language?: string,
    public createdBy?: string,
    public createdDate?: Moment,
    public lastModifiedBy?: string,
    public lastModifiedDate?: Moment,
    public ordereds?: IOrdered[],
    public pays?: IPay[]
  ) {}
}
