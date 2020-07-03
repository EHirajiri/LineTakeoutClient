import { Moment } from 'moment';

export interface IOrdered {
  id?: string;
  quantity?: number;
  unitPrice?: number;
  totalFee?: number;
  createdBy?: string;
  createdDate?: Moment;
  lastModifiedBy?: string;
  lastModifiedDate?: Moment;
  customerId?: number;
  customerUserId?: string;
  itemId?: number;
  itemName?: string;
}

export class Ordered implements IOrdered {
  constructor(
    public id?: string,
    public quantity?: number,
    public unitPrice?: number,
    public totalFee?: number,
    public createdBy?: string,
    public createdDate?: Moment,
    public lastModifiedBy?: string,
    public lastModifiedDate?: Moment,
    public customerId?: number,
    public itemId?: number
  ) {}
}
