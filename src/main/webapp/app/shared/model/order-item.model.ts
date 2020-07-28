import { IOrdered } from 'app/shared/model/ordered.model';

export interface IOrderItem {
  id?: number;
  name?: string;
  price?: number;
  quantity?: number;
  ordereds?: IOrdered[];
  itemId?: number;
}

export class OrderItem implements IOrderItem {
  constructor(
    public id?: number,
    public name?: string,
    public price?: number,
    public quantity?: number,
    public ordereds?: IOrdered[],
    public itemId?: number
  ) {}
}
