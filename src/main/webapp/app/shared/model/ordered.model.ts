import { Moment } from 'moment';
import { IOrderItem } from 'app/shared/model/order-item.model';
import { DeliveryState } from 'app/shared/model/enumerations/delivery-state.model';

export interface IOrdered {
  id?: number;
  orderId?: string;
  totalFee?: number;
  deliveryState?: DeliveryState;
  deliveryDate?: Moment;
  createdBy?: string;
  createdDate?: Moment;
  lastModifiedBy?: string;
  lastModifiedDate?: Moment;
  customerId?: number;
  customerNickname?: string;
  orderItems?: IOrderItem[];
}

export class Ordered implements IOrdered {
  constructor(
    public id?: number,
    public orderId?: string,
    public totalFee?: number,
    public deliveryState?: DeliveryState,
    public deliveryDate?: Moment,
    public createdBy?: string,
    public createdDate?: Moment,
    public lastModifiedBy?: string,
    public lastModifiedDate?: Moment,
    public customerId?: number,
    public orderItems?: IOrderItem[]
  ) {}
}
