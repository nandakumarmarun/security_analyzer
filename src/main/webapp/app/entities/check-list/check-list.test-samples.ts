import { ICheckList, NewCheckList } from './check-list.model';

export const sampleWithRequiredData: ICheckList = {
  id: 24113,
};

export const sampleWithPartialData: ICheckList = {
  id: 13788,
  name: 'faithfully oof',
};

export const sampleWithFullData: ICheckList = {
  id: 27112,
  name: 'eagle',
};

export const sampleWithNewData: NewCheckList = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
