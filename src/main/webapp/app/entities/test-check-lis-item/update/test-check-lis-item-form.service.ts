import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { ITestCheckLisItem, NewTestCheckLisItem } from '../test-check-lis-item.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts ITestCheckLisItem for edit and NewTestCheckLisItemFormGroupInput for create.
 */
type TestCheckLisItemFormGroupInput = ITestCheckLisItem | PartialWithRequiredKeyOf<NewTestCheckLisItem>;

type TestCheckLisItemFormDefaults = Pick<NewTestCheckLisItem, 'id' | 'marked'>;

type TestCheckLisItemFormGroupContent = {
  id: FormControl<ITestCheckLisItem['id'] | NewTestCheckLisItem['id']>;
  marked: FormControl<ITestCheckLisItem['marked']>;
  checklistitem: FormControl<ITestCheckLisItem['checklistitem']>;
  testCheckList: FormControl<ITestCheckLisItem['testCheckList']>;
};

export type TestCheckLisItemFormGroup = FormGroup<TestCheckLisItemFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class TestCheckLisItemFormService {
  createTestCheckLisItemFormGroup(testCheckLisItem: TestCheckLisItemFormGroupInput = { id: null }): TestCheckLisItemFormGroup {
    const testCheckLisItemRawValue = {
      ...this.getFormDefaults(),
      ...testCheckLisItem,
    };
    return new FormGroup<TestCheckLisItemFormGroupContent>({
      id: new FormControl(
        { value: testCheckLisItemRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      marked: new FormControl(testCheckLisItemRawValue.marked),
      checklistitem: new FormControl(testCheckLisItemRawValue.checklistitem),
      testCheckList: new FormControl(testCheckLisItemRawValue.testCheckList),
    });
  }

  getTestCheckLisItem(form: TestCheckLisItemFormGroup): ITestCheckLisItem | NewTestCheckLisItem {
    return form.getRawValue() as ITestCheckLisItem | NewTestCheckLisItem;
  }

  resetForm(form: TestCheckLisItemFormGroup, testCheckLisItem: TestCheckLisItemFormGroupInput): void {
    const testCheckLisItemRawValue = { ...this.getFormDefaults(), ...testCheckLisItem };
    form.reset(
      {
        ...testCheckLisItemRawValue,
        id: { value: testCheckLisItemRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): TestCheckLisItemFormDefaults {
    return {
      id: null,
      marked: false,
    };
  }
}
