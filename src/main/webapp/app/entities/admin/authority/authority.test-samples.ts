import { IAuthority, NewAuthority } from './authority.model';

export const sampleWithRequiredData: IAuthority = {
  name: '550feb00-5d60-4811-abc2-fb874cdabb04',
};

export const sampleWithPartialData: IAuthority = {
  name: 'cf00ef2e-2c8c-4884-91cf-845065ccce71',
};

export const sampleWithFullData: IAuthority = {
  name: '59823517-4267-47cd-b3f8-882a1f7cdc72',
};

export const sampleWithNewData: NewAuthority = {
  name: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
