export interface IOrdered {
  id?: number;
  quantity?: number;
  totalFee?: number;
  customerId?: number;
  itemId?: number;
}

export class Ordered implements IOrdered {
  constructor(public id?: number, public quantity?: number, public totalFee?: number, public customerId?: number, public itemId?: number) {}
}
