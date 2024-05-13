import { ICountry } from 'app/entities/country/country.model';
import { IState } from 'app/entities/state/state.model';
import { IDistrict } from 'app/entities/district/district.model';
import { ICity } from 'app/entities/city/city.model';
import { ILocation } from 'app/entities/location/location.model';

export interface IUserAttributes {
  id: number;
  name?: string | null;
  phone?: string | null;
  email?: string | null;
  address?: string | null;
  country?: ICountry | null;
  state?: IState | null;
  district?: IDistrict | null;
  city?: ICity | null;
  location?: ILocation | null;
}

export type NewUserAttributes = Omit<IUserAttributes, 'id'> & { id: null };
