import { ICity, NewCity } from './city.model';

export const sampleWithRequiredData: ICity = {
  id: 16191,
};

export const sampleWithPartialData: ICity = {
  id: 3146,
  name: 'immediately flub beyond',
};

export const sampleWithFullData: ICity = {
  id: 18874,
  name: 'athwart knowing ew',
};

export const sampleWithNewData: NewCity = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
