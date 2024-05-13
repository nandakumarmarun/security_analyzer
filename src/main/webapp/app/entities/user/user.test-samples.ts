import { IUser } from './user.model';

export const sampleWithRequiredData: IUser = {
  id: 19629,
  login: 'hA',
};

export const sampleWithPartialData: IUser = {
  id: 11794,
  login: '_S',
};

export const sampleWithFullData: IUser = {
  id: 16790,
  login: 'wQ1Tr3@zmOFFG\\zbptQSy\\^JAy',
};
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
