import { IPriceRoom } from 'app/shared/model/price-room.model';
import { IInvoice } from 'app/shared/model/invoice.model';

export interface IDetailInvoiceRoom {
  id?: number;
  price?: number | null;
  priceRoom?: IPriceRoom | null;
  invoice?: IInvoice | null;
}

export const defaultValue: Readonly<IDetailInvoiceRoom> = {};
