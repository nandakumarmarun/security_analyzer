import { IApplicationUser } from 'app/entities/application-user/application-user.model';
import { SecurityLevel } from 'app/entities/enumerations/security-level.model';

export interface ISecurityTest {
  id: number;
  testStatus?: string | null;
  testScore?: number | null;
  securityLevel?: keyof typeof SecurityLevel | null;
  applicationUser?: IApplicationUser | null;
}

export type NewSecurityTest = Omit<ISecurityTest, 'id'> & { id: null };
