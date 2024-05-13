import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { ICheckList, NewCheckList } from '../check-list.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts ICheckList for edit and NewCheckListFormGroupInput for create.
 */
type CheckListFormGroupInput = ICheckList | PartialWithRequiredKeyOf<NewCheckList>;

type CheckListFormDefaults = Pick<NewCheckList, 'id'>;

type CheckListFormGroupContent = {
  id: FormControl<ICheckList['id'] | NewCheckList['id']>;
  name: FormControl<ICheckList['name']>;
};

export type CheckListFormGroup = FormGroup<CheckListFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class CheckListFormService {
  createCheckListFormGroup(checkList: CheckListFormGroupInput = { id: null }): CheckListFormGroup {
    const checkListRawValue = {
      ...this.getFormDefaults(),
      ...checkList,
    };
    return new FormGroup<CheckListFormGroupContent>({
      id: new FormControl(
        { value: checkListRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      name: new FormControl(checkListRawValue.name),
    });
  }

  getCheckList(form: CheckListFormGroup): ICheckList | NewCheckList {
    return form.getRawValue() as ICheckList | NewCheckList;
  }

  resetForm(form: CheckListFormGroup, checkList: CheckListFormGroupInput): void {
    const checkListRawValue = { ...this.getFormDefaults(), ...checkList };
    form.reset(
      {
        ...checkListRawValue,
        id: { value: checkListRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): CheckListFormDefaults {
    return {
      id: null,
    };
  }
}
