import { IRoomHotel } from 'app/shared/model/room-hotel.model';

export interface IPriceRoom {
  id?: number;
  price?: number | null;
  unit?: string | null;
  roomHotel?: IRoomHotel | null;
}

export const defaultValue: Readonly<IPriceRoom> = {};
