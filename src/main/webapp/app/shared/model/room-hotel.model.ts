import { ITypeRoom } from 'app/shared/model/type-room.model';
import { IImageRoom } from 'app/shared/model/image-room.model';

export interface IRoomHotel {
  id?: number;
  roomName?: string | null;
  description?: string | null;
  roomStatus?: number | null;
  typeRoom?: ITypeRoom | null;
  imageRooms?: IImageRoom[] | null;
}

export const defaultValue: Readonly<IRoomHotel> = {};
