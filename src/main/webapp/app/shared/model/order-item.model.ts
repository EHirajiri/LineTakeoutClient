import { Moment } from 'moment';

export interface IOrderItem {
  id?: number;
  name?: string;
  price?: number;
  quantity?: number;
  totalFee?: number;
  createdBy?: string;
  createdDate?: Moment;
  lastModifiedBy?: string;
  lastModifiedDate?: Moment;
  orderedId?: number;
}

export class OrderItem implements IOrderItem {
  constructor(
    public id?: number,
    public name?: string,
    public price?: number,
    public quantity?: number,
    public totalFee?: number,
    public createdBy?: string,
    public createdDate?: Moment,
    public lastModifiedBy?: string,
    public lastModifiedDate?: Moment,
    public orderedId?: number
  ) {}
}
