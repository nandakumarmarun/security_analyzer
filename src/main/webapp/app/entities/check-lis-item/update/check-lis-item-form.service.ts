import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { ICheckLisItem, NewCheckLisItem } from '../check-lis-item.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts ICheckLisItem for edit and NewCheckLisItemFormGroupInput for create.
 */
type CheckLisItemFormGroupInput = ICheckLisItem | PartialWithRequiredKeyOf<NewCheckLisItem>;

type CheckLisItemFormDefaults = Pick<NewCheckLisItem, 'id'>;

type CheckLisItemFormGroupContent = {
  id: FormControl<ICheckLisItem['id'] | NewCheckLisItem['id']>;
  name: FormControl<ICheckLisItem['name']>;
  value: FormControl<ICheckLisItem['value']>;
  checkList: FormControl<ICheckLisItem['checkList']>;
};

export type CheckLisItemFormGroup = FormGroup<CheckLisItemFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class CheckLisItemFormService {
  createCheckLisItemFormGroup(checkLisItem: CheckLisItemFormGroupInput = { id: null }): CheckLisItemFormGroup {
    const checkLisItemRawValue = {
      ...this.getFormDefaults(),
      ...checkLisItem,
    };
    return new FormGroup<CheckLisItemFormGroupContent>({
      id: new FormControl(
        { value: checkLisItemRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      name: new FormControl(checkLisItemRawValue.name),
      value: new FormControl(checkLisItemRawValue.value),
      checkList: new FormControl(checkLisItemRawValue.checkList),
    });
  }

  getCheckLisItem(form: CheckLisItemFormGroup): ICheckLisItem | NewCheckLisItem {
    return form.getRawValue() as ICheckLisItem | NewCheckLisItem;
  }

  resetForm(form: CheckLisItemFormGroup, checkLisItem: CheckLisItemFormGroupInput): void {
    const checkLisItemRawValue = { ...this.getFormDefaults(), ...checkLisItem };
    form.reset(
      {
        ...checkLisItemRawValue,
        id: { value: checkLisItemRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): CheckLisItemFormDefaults {
    return {
      id: null,
    };
  }
}
