import { ITestCheckList, NewTestCheckList } from './test-check-list.model';

export const sampleWithRequiredData: ITestCheckList = {
  id: 31262,
};

export const sampleWithPartialData: ITestCheckList = {
  id: 11152,
};

export const sampleWithFullData: ITestCheckList = {
  id: 6274,
};

export const sampleWithNewData: NewTestCheckList = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
