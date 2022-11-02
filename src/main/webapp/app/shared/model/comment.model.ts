import dayjs from 'dayjs';
import { IServices } from 'app/shared/model/services.model';
import { IUser } from 'app/shared/model/user.model';

export interface IComment {
  id?: number;
  content?: string | null;
  createdDate?: string | null;
  vote?: number | null;
  services?: IServices | null;
  user?: IUser | null;
}

export const defaultValue: Readonly<IComment> = {};
