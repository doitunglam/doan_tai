import { IServices } from 'app/shared/model/services.model';

export interface ICategory {
  id?: number;
  categoryName?: string | null;
  services?: IServices[] | null;
}

export const defaultValue: Readonly<ICategory> = {};
