import { Moment } from 'moment';
import { PayState } from 'app/shared/model/enumerations/pay-state.model';
import { DeliveryState } from 'app/shared/model/enumerations/delivery-state.model';

export interface IPay {
  id?: number;
  transactionId?: number;
  title?: string;
  payState?: PayState;
  deliveryState?: DeliveryState;
  paidDate?: Moment;
  deliveryDate?: Moment;
  amount?: number;
  createdBy?: string;
  createdDate?: Moment;
  lastModifiedBy?: string;
  lastModifiedDate?: Moment;
  customerId?: number;
  orderedId?: number;
}

export class Pay implements IPay {
  constructor(
    public id?: number,
    public transactionId?: number,
    public title?: string,
    public payState?: PayState,
    public deliveryState?: DeliveryState,
    public paidDate?: Moment,
    public deliveryDate?: Moment,
    public amount?: number,
    public createdBy?: string,
    public createdDate?: Moment,
    public lastModifiedBy?: string,
    public lastModifiedDate?: Moment,
    public customerId?: number,
    public orderedId?: number
  ) {}
}
