import { ICountry, NewCountry } from './country.model';

export const sampleWithRequiredData: ICountry = {
  id: 32010,
};

export const sampleWithPartialData: ICountry = {
  id: 23391,
};

export const sampleWithFullData: ICountry = {
  id: 29700,
  name: 'comedy rabbit beside',
};

export const sampleWithNewData: NewCountry = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
