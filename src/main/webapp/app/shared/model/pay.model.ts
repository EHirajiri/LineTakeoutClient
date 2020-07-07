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
  receivedDate?: Moment;
  createdBy?: string;
  createdDate?: Moment;
  lastModifiedBy?: string;
  lastModifiedDate?: Moment;
  orderedId?: number;
  customerId?: number;
}

export class Pay implements IPay {
  constructor(
    public id?: number,
    public transactionId?: number,
    public title?: string,
    public payState?: PayState,
    public deliveryState?: DeliveryState,
    public paidDate?: Moment,
    public receivedDate?: Moment,
    public createdBy?: string,
    public createdDate?: Moment,
    public lastModifiedBy?: string,
    public lastModifiedDate?: Moment,
    public orderedId?: number,
    public customerId?: number
  ) {}
}
