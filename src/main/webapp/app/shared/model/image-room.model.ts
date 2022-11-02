import { IRoomHotel } from 'app/shared/model/room-hotel.model';

export interface IImageRoom {
  id?: number;
  linkImage?: string | null;
  roomHotel?: IRoomHotel | null;
}

export const defaultValue: Readonly<IImageRoom> = {};
