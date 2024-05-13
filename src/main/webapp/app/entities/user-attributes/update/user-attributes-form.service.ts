import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { IUserAttributes, NewUserAttributes } from '../user-attributes.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IUserAttributes for edit and NewUserAttributesFormGroupInput for create.
 */
type UserAttributesFormGroupInput = IUserAttributes | PartialWithRequiredKeyOf<NewUserAttributes>;

type UserAttributesFormDefaults = Pick<NewUserAttributes, 'id'>;

type UserAttributesFormGroupContent = {
  id: FormControl<IUserAttributes['id'] | NewUserAttributes['id']>;
  name: FormControl<IUserAttributes['name']>;
  phone: FormControl<IUserAttributes['phone']>;
  email: FormControl<IUserAttributes['email']>;
  address: FormControl<IUserAttributes['address']>;
  country: FormControl<IUserAttributes['country']>;
  state: FormControl<IUserAttributes['state']>;
  district: FormControl<IUserAttributes['district']>;
  city: FormControl<IUserAttributes['city']>;
  location: FormControl<IUserAttributes['location']>;
};

export type UserAttributesFormGroup = FormGroup<UserAttributesFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class UserAttributesFormService {
  createUserAttributesFormGroup(userAttributes: UserAttributesFormGroupInput = { id: null }): UserAttributesFormGroup {
    const userAttributesRawValue = {
      ...this.getFormDefaults(),
      ...userAttributes,
    };
    return new FormGroup<UserAttributesFormGroupContent>({
      id: new FormControl(
        { value: userAttributesRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      name: new FormControl(userAttributesRawValue.name, {
        validators: [Validators.required, Validators.minLength(1), Validators.maxLength(255)],
      }),
      phone: new FormControl(userAttributesRawValue.phone),
      email: new FormControl(userAttributesRawValue.email),
      address: new FormControl(userAttributesRawValue.address),
      country: new FormControl(userAttributesRawValue.country),
      state: new FormControl(userAttributesRawValue.state),
      district: new FormControl(userAttributesRawValue.district),
      city: new FormControl(userAttributesRawValue.city),
      location: new FormControl(userAttributesRawValue.location),
    });
  }

  getUserAttributes(form: UserAttributesFormGroup): IUserAttributes | NewUserAttributes {
    return form.getRawValue() as IUserAttributes | NewUserAttributes;
  }

  resetForm(form: UserAttributesFormGroup, userAttributes: UserAttributesFormGroupInput): void {
    const userAttributesRawValue = { ...this.getFormDefaults(), ...userAttributes };
    form.reset(
      {
        ...userAttributesRawValue,
        id: { value: userAttributesRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): UserAttributesFormDefaults {
    return {
      id: null,
    };
  }
}
