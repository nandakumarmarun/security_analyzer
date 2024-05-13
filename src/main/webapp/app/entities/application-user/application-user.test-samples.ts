import { IApplicationUser, NewApplicationUser } from './application-user.model';

export const sampleWithRequiredData: IApplicationUser = {
  id: 20177,
};

export const sampleWithPartialData: IApplicationUser = {
  id: 31795,
};

export const sampleWithFullData: IApplicationUser = {
  id: 29634,
  name: 'willfully',
};

export const sampleWithNewData: NewApplicationUser = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
