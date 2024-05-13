import { IUserAttributes, NewUserAttributes } from './user-attributes.model';

export const sampleWithRequiredData: IUserAttributes = {
  id: 11228,
  name: 'pall exterior pioneer',
};

export const sampleWithPartialData: IUserAttributes = {
  id: 5817,
  name: 'formalise disdain tech',
};

export const sampleWithFullData: IUserAttributes = {
  id: 13538,
  name: 'overconfidently fatally dizzy',
  phone: '731-496-0472 x4642',
  email: 'Corine69@hotmail.com',
  address: 'ick ouch',
};

export const sampleWithNewData: NewUserAttributes = {
  name: 'wisely sunday smoothly',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
