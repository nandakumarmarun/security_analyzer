import { ICheckLisItem, NewCheckLisItem } from './check-lis-item.model';

export const sampleWithRequiredData: ICheckLisItem = {
  id: 26780,
};

export const sampleWithPartialData: ICheckLisItem = {
  id: 26995,
  value: 11138.43,
};

export const sampleWithFullData: ICheckLisItem = {
  id: 6952,
  name: 'before instead now',
  value: 19948.28,
};

export const sampleWithNewData: NewCheckLisItem = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
