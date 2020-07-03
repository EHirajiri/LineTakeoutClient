import { Moment } from 'moment';

export interface ICustomer {
  id?: number;
  userId?: string;
  nickname?: string;
  language?: string;
  createdBy?: string;
  createdDate?: Moment;
  lastModifiedBy?: string;
  lastModifiedDate?: Moment;
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
    public lastModifiedDate?: Moment
  ) {}
}
