import { IState } from 'app/entities/state/state.model';

export interface IDistrict {
  id: number;
  name?: string | null;
  state?: IState | null;
}

export type NewDistrict = Omit<IDistrict, 'id'> & { id: null };
