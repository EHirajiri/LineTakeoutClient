import { Moment } from 'moment';
import { DeliveryState } from 'app/shared/model/enumerations/delivery-state.model';

export interface IOrdered {
  id?: number;
  orderId?: string;
  quantity?: number;
  unitPrice?: number;
  totalFee?: number;
  deliveryState?: DeliveryState;
  deliveryDate?: Moment;
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
    public orderId?: string,
    public quantity?: number,
    public unitPrice?: number,
    public totalFee?: number,
    public deliveryState?: DeliveryState,
    public deliveryDate?: Moment,
    public createdBy?: string,
    public createdDate?: Moment,
    public lastModifiedBy?: string,
    public lastModifiedDate?: Moment,
    public customerId?: number,
    public itemId?: number
  ) {}
}
