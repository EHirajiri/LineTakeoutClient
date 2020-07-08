import { Moment } from 'moment';
import { IOrdered } from 'app/shared/model/ordered.model';

export interface IItem {
  id?: number;
  name?: string;
  description?: any;
  price?: number;
  imageUrl?: string;
  createdBy?: string;
  createdDate?: Moment;
  lastModifiedBy?: string;
  lastModifiedDate?: Moment;
  ordereds?: IOrdered[];
}

export class Item implements IItem {
  constructor(
    public id?: number,
    public name?: string,
    public description?: any,
    public price?: number,
    public imageUrl?: string,
    public createdBy?: string,
    public createdDate?: Moment,
    public lastModifiedBy?: string,
    public lastModifiedDate?: Moment,
    public ordereds?: IOrdered[]
  ) {}
}
