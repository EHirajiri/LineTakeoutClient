export interface IItem {
  id?: number;
  name?: string;
  description?: any;
  price?: number;
  image?: string;
}

export class Item implements IItem {
  constructor(public id?: number, public name?: string, public description?: any, public price?: number, public image?: string) {}
}
