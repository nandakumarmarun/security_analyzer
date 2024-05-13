import { IState, NewState } from './state.model';

export const sampleWithRequiredData: IState = {
  id: 19328,
};

export const sampleWithPartialData: IState = {
  id: 7299,
  name: 'rumple psst',
};

export const sampleWithFullData: IState = {
  id: 9493,
  name: 'indeed joyfully compulsion',
};

export const sampleWithNewData: NewState = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
