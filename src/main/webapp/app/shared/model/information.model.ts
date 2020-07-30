import { Moment } from 'moment';

export interface IInformation {
  id?: number;
  key?: string;
  value?: string;
  createdBy?: string;
  createdDate?: Moment;
  lastModifiedBy?: string;
  lastModifiedDate?: Moment;
}

export class Information implements IInformation {
  constructor(
    public id?: number,
    public key?: string,
    public value?: string,
    public createdBy?: string,
    public createdDate?: Moment,
    public lastModifiedBy?: string,
    public lastModifiedDate?: Moment
  ) {}
}
