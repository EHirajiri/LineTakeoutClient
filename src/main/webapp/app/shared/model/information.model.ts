export interface IInformation {
  id?: number;
  key?: string;
  value?: string;
}

export class Information implements IInformation {
  constructor(public id?: number, public key?: string, public value?: string) {}
}
