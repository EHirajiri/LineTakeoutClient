import { Moment } from 'moment';
import { IPay } from 'app/shared/model/pay.model';

export interface IOrdered {
  id?: number;
  orderId?: string;
  quantity?: number;
  unitPrice?: number;
  totalFee?: number;
  createdBy?: string;
  createdDate?: Moment;
  lastModifiedBy?: string;
  lastModifiedDate?: Moment;
  pays?: IPay[];
  customerId?: number;
  itemId?: number;
}

export class Ordered implements IOrdered {
  constructor(
    public id?: number,
    public orderId?: string,
    public quantity?: number,
    public unitPrice?: number,
    public totalFee?: number,
    public createdBy?: string,
    public createdDate?: Moment,
    public lastModifiedBy?: string,
    public lastModifiedDate?: Moment,
    public pays?: IPay[],
    public customerId?: number,
    public itemId?: number
  ) {}
}
