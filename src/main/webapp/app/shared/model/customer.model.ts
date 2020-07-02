export interface ICustomer {
  id?: number;
  userId?: string;
  nickname?: string;
  language?: string;
  createdDate?: Date;
}

export class Customer implements ICustomer {
  constructor(public id?: number, public userId?: string, public nickname?: string, public language?: string) {}
}
