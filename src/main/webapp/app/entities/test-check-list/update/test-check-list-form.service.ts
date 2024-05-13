import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { ITestCheckList, NewTestCheckList } from '../test-check-list.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts ITestCheckList for edit and NewTestCheckListFormGroupInput for create.
 */
type TestCheckListFormGroupInput = ITestCheckList | PartialWithRequiredKeyOf<NewTestCheckList>;

type TestCheckListFormDefaults = Pick<NewTestCheckList, 'id'>;

type TestCheckListFormGroupContent = {
  id: FormControl<ITestCheckList['id'] | NewTestCheckList['id']>;
  checkList: FormControl<ITestCheckList['checkList']>;
  securityTest: FormControl<ITestCheckList['securityTest']>;
};

export type TestCheckListFormGroup = FormGroup<TestCheckListFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class TestCheckListFormService {
  createTestCheckListFormGroup(testCheckList: TestCheckListFormGroupInput = { id: null }): TestCheckListFormGroup {
    const testCheckListRawValue = {
      ...this.getFormDefaults(),
      ...testCheckList,
    };
    return new FormGroup<TestCheckListFormGroupContent>({
      id: new FormControl(
        { value: testCheckListRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      checkList: new FormControl(testCheckListRawValue.checkList),
      securityTest: new FormControl(testCheckListRawValue.securityTest),
    });
  }

  getTestCheckList(form: TestCheckListFormGroup): ITestCheckList | NewTestCheckList {
    return form.getRawValue() as ITestCheckList | NewTestCheckList;
  }

  resetForm(form: TestCheckListFormGroup, testCheckList: TestCheckListFormGroupInput): void {
    const testCheckListRawValue = { ...this.getFormDefaults(), ...testCheckList };
    form.reset(
      {
        ...testCheckListRawValue,
        id: { value: testCheckListRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): TestCheckListFormDefaults {
    return {
      id: null,
    };
  }
}
