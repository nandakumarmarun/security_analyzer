import { IState } from 'app/entities/state/state.model';
import { ICity } from 'app/entities/city/city.model';
import { IDistrict } from 'app/entities/district/district.model';

export interface ILocation {
  id: number;
  name?: string | null;
  state?: IState | null;
  city?: ICity | null;
  district?: IDistrict | null;
}

export type NewLocation = Omit<ILocation, 'id'> & { id: null };
