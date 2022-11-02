import dayjs from 'dayjs';
import { IUser } from 'app/shared/model/user.model';

export interface IInvoice {
  id?: number;
  createdDate?: string | null;
  cost?: number | null;
  paycode?: string | null;
  user?: IUser | null;
}

export const defaultValue: Readonly<IInvoice> = {};
