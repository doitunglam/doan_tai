import { IInvoice } from 'app/shared/model/invoice.model';
import { IServices } from 'app/shared/model/services.model';

export interface IDetailInvoice {
  id?: number;
  quantity?: number | null;
  price?: number | null;
  invoice?: IInvoice | null;
  services?: IServices | null;
}

export const defaultValue: Readonly<IDetailInvoice> = {};
