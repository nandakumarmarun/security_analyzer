import { ITestCheckLisItem, NewTestCheckLisItem } from './test-check-lis-item.model';

export const sampleWithRequiredData: ITestCheckLisItem = {
  id: 13924,
};

export const sampleWithPartialData: ITestCheckLisItem = {
  id: 24328,
  marked: true,
};

export const sampleWithFullData: ITestCheckLisItem = {
  id: 24476,
  marked: false,
};

export const sampleWithNewData: NewTestCheckLisItem = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
