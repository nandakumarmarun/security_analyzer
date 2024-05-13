import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { ISecurityTest, NewSecurityTest } from '../security-test.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts ISecurityTest for edit and NewSecurityTestFormGroupInput for create.
 */
type SecurityTestFormGroupInput = ISecurityTest | PartialWithRequiredKeyOf<NewSecurityTest>;

type SecurityTestFormDefaults = Pick<NewSecurityTest, 'id'>;

type SecurityTestFormGroupContent = {
  id: FormControl<ISecurityTest['id'] | NewSecurityTest['id']>;
  testStatus: FormControl<ISecurityTest['testStatus']>;
  testScore: FormControl<ISecurityTest['testScore']>;
  securityLevel: FormControl<ISecurityTest['securityLevel']>;
  applicationUser: FormControl<ISecurityTest['applicationUser']>;
};

export type SecurityTestFormGroup = FormGroup<SecurityTestFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class SecurityTestFormService {
  createSecurityTestFormGroup(securityTest: SecurityTestFormGroupInput = { id: null }): SecurityTestFormGroup {
    const securityTestRawValue = {
      ...this.getFormDefaults(),
      ...securityTest,
    };
    return new FormGroup<SecurityTestFormGroupContent>({
      id: new FormControl(
        { value: securityTestRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      testStatus: new FormControl(securityTestRawValue.testStatus),
      testScore: new FormControl(securityTestRawValue.testScore),
      securityLevel: new FormControl(securityTestRawValue.securityLevel),
      applicationUser: new FormControl(securityTestRawValue.applicationUser),
    });
  }

  getSecurityTest(form: SecurityTestFormGroup): ISecurityTest | NewSecurityTest {
    return form.getRawValue() as ISecurityTest | NewSecurityTest;
  }

  resetForm(form: SecurityTestFormGroup, securityTest: SecurityTestFormGroupInput): void {
    const securityTestRawValue = { ...this.getFormDefaults(), ...securityTest };
    form.reset(
      {
        ...securityTestRawValue,
        id: { value: securityTestRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): SecurityTestFormDefaults {
    return {
      id: null,
    };
  }
}
