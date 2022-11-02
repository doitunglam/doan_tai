import { IServices } from 'app/shared/model/services.model';

export interface IImageServices {
  id?: number;
  linkimage?: string | null;
  services?: IServices | null;
}

export const defaultValue: Readonly<IImageServices> = {};
