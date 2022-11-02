import { IUser } from 'app/shared/model/user.model';

export interface IHotel {
  id?: number;
  hotelName?: string | null;
  address?: string | null;
  phone?: string | null;
  token?: string | null;
  active?: number | null;
  users?: IUser[] | null;
}

export const defaultValue: Readonly<IHotel> = {};
