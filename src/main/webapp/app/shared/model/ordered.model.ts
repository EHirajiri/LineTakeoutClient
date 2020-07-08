import { Moment } from 'moment';
import { IItem } from 'app/shared/model/item.model';

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
  payId?: number;
  items?: IItem[];
  customerId?: number;
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
    public payId?: number,
    public items?: IItem[],
    public customerId?: number
  ) {}
}
