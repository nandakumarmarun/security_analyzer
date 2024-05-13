import { ICheckList } from 'app/entities/check-list/check-list.model';

export interface ICheckLisItem {
  id: number;
  name?: string | null;
  value?: number | null;
  checkList?: ICheckList | null;
}

export type NewCheckLisItem = Omit<ICheckLisItem, 'id'> & { id: null };
