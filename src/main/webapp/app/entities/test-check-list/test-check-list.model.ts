import { ICheckList } from 'app/entities/check-list/check-list.model';
import { ISecurityTest } from 'app/entities/security-test/security-test.model';

export interface ITestCheckList {
  id: number;
  checkList?: ICheckList | null;
  securityTest?: ISecurityTest | null;
}

export type NewTestCheckList = Omit<ITestCheckList, 'id'> & { id: null };
