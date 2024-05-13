import { ICountry } from 'app/entities/country/country.model';

export interface IState {
  id: number;
  name?: string | null;
  country?: ICountry | null;
}

export type NewState = Omit<IState, 'id'> & { id: null };
