import { IHotel } from 'app/shared/model/hotel.model';

export interface ITypeRoom {
  id?: number;
  typeName?: string | null;
  hotel?: IHotel | null;
}

export const defaultValue: Readonly<ITypeRoom> = {};
