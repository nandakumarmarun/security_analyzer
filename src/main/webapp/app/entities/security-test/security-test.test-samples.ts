import { ISecurityTest, NewSecurityTest } from './security-test.model';

export const sampleWithRequiredData: ISecurityTest = {
  id: 15954,
};

export const sampleWithPartialData: ISecurityTest = {
  id: 21456,
  testStatus: 'whoa vacuum',
};

export const sampleWithFullData: ISecurityTest = {
  id: 3311,
  testStatus: 'plan',
  testScore: 19569.64,
  securityLevel: 'MODERATE',
};

export const sampleWithNewData: NewSecurityTest = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
