import { ILocation, NewLocation } from './location.model';

export const sampleWithRequiredData: ILocation = {
  id: 459,
};

export const sampleWithPartialData: ILocation = {
  id: 23972,
  name: 'ordinary cheerful which',
};

export const sampleWithFullData: ILocation = {
  id: 20746,
  name: 'how',
};

export const sampleWithNewData: NewLocation = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
