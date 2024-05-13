import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { IState, NewState } from '../state.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IState for edit and NewStateFormGroupInput for create.
 */
type StateFormGroupInput = IState | PartialWithRequiredKeyOf<NewState>;

type StateFormDefaults = Pick<NewState, 'id'>;

type StateFormGroupContent = {
  id: FormControl<IState['id'] | NewState['id']>;
  name: FormControl<IState['name']>;
  country: FormControl<IState['country']>;
};

export type StateFormGroup = FormGroup<StateFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class StateFormService {
  createStateFormGroup(state: StateFormGroupInput = { id: null }): StateFormGroup {
    const stateRawValue = {
      ...this.getFormDefaults(),
      ...state,
    };
    return new FormGroup<StateFormGroupContent>({
      id: new FormControl(
        { value: stateRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      name: new FormControl(stateRawValue.name),
      country: new FormControl(stateRawValue.country),
    });
  }

  getState(form: StateFormGroup): IState | NewState {
    return form.getRawValue() as IState | NewState;
  }

  resetForm(form: StateFormGroup, state: StateFormGroupInput): void {
    const stateRawValue = { ...this.getFormDefaults(), ...state };
    form.reset(
      {
        ...stateRawValue,
        id: { value: stateRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): StateFormDefaults {
    return {
      id: null,
    };
  }
}
