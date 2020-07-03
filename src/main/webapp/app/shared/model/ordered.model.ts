import { Moment } from 'moment';

export interface IOrdered {
  id?: number;
  quantity?: number;
  totalFee?: number;
  createdBy?: string;
  createdDate?: Moment;
  lastModifiedBy?: string;
  lastModifiedDate?: Moment;
  customerId?: number;
  itemId?: number;
}

export class Ordered implements IOrdered {
  constructor(
    public id?: number,
    public quantity?: number,
    public totalFee?: number,
    public createdBy?: string,
    public createdDate?: Moment,
    public lastModifiedBy?: string,
    public lastModifiedDate?: Moment,
    public customerId?: number,
    public itemId?: number
  ) {}
}
