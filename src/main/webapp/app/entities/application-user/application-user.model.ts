import { IUser } from 'app/entities/user/user.model';
import { IUserAttributes } from 'app/entities/user-attributes/user-attributes.model';

export interface IApplicationUser {
  id: number;
  name?: string | null;
  internalUser?: Pick<IUser, 'id' | 'login'> | null;
  userAttributes?: IUserAttributes | null;
}

export type NewApplicationUser = Omit<IApplicationUser, 'id'> & { id: null };
