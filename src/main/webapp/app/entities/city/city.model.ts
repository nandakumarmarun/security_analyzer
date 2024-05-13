import { ICountry } from 'app/entities/country/country.model';

export interface ICity {
  id: number;
  name?: string | null;
  country?: ICountry | null;
}

export type NewCity = Omit<ICity, 'id'> & { id: null };
