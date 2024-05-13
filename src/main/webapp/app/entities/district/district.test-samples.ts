import { IDistrict, NewDistrict } from './district.model';

export const sampleWithRequiredData: IDistrict = {
  id: 30650,
};

export const sampleWithPartialData: IDistrict = {
  id: 3097,
  name: 'infantilize for',
};

export const sampleWithFullData: IDistrict = {
  id: 2047,
  name: 'testimonial',
};

export const sampleWithNewData: NewDistrict = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
