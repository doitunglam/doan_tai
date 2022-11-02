import { IUser } from 'app/shared/model/user.model';

export interface IPay {
  id?: number;
  code?: string | null;
  cost?: number | null;
  user?: IUser | null;
}

export const defaultValue: Readonly<IPay> = {};
