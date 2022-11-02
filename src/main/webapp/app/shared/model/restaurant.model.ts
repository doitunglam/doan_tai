import { IUser } from 'app/shared/model/user.model';
import { IServices } from 'app/shared/model/services.model';

export interface IRestaurant {
  id?: number;
  resName?: string | null;
  address?: string | null;
  phone?: string | null;
  token?: string | null;
  linkweb?: string | null;
  users?: IUser[] | null;
  services?: IServices[] | null;
}

export const defaultValue: Readonly<IRestaurant> = {};
