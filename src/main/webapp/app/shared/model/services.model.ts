import { IRestaurant } from 'app/shared/model/restaurant.model';
import { IImageServices } from 'app/shared/model/image-services.model';
import { ICategory } from 'app/shared/model/category.model';

export interface IServices {
  id?: number;
  serviceName?: string | null;
  price?: number | null;
  unit?: string | null;
  quantity?: number | null;
  address?: string | null;
  restaurant?: IRestaurant | null;
  imageServices?: IImageServices[] | null;
  category?: ICategory | null;
}

export const defaultValue: Readonly<IServices> = {};
