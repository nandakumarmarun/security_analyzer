import { ICheckLisItem } from 'app/entities/check-lis-item/check-lis-item.model';
import { ITestCheckList } from 'app/entities/test-check-list/test-check-list.model';

export interface ITestCheckLisItem {
  id: number;
  marked?: boolean | null;
  checklistitem?: ICheckLisItem | null;
  testCheckList?: ITestCheckList | null;
}

export type NewTestCheckLisItem = Omit<ITestCheckLisItem, 'id'> & { id: null };
