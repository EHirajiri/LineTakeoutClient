import { Moment } from 'moment';
import { IOrdered } from 'app/shared/model/ordered.model';

export interface IOrderItem {
  id?: number;
  name?: string;
  price?: number;
  quantity?: number;
  createdBy?: string;
  createdDate?: Moment;
  lastModifiedBy?: string;
  lastModifiedDate?: Moment;
  ordereds?: IOrdered[];
  itemId?: number;
}

export class OrderItem implements IOrderItem {
  constructor(
    public id?: number,
    public name?: string,
    public price?: number,
    public quantity?: number,
    public createdBy?: string,
    public createdDate?: Moment,
    public lastModifiedBy?: string,
    public lastModifiedDate?: Moment,
    public ordereds?: IOrdered[],
    public itemId?: number
  ) {}
}
