import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../user-attributes.test-samples';

import { UserAttributesFormService } from './user-attributes-form.service';

describe('UserAttributes Form Service', () => {
  let service: UserAttributesFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(UserAttributesFormService);
  });

  describe('Service methods', () => {
    describe('createUserAttributesFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createUserAttributesFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            name: expect.any(Object),
            phone: expect.any(Object),
            email: expect.any(Object),
            address: expect.any(Object),
            country: expect.any(Object),
            state: expect.any(Object),
            district: expect.any(Object),
            city: expect.any(Object),
            location: expect.any(Object),
          }),
        );
      });

      it('passing IUserAttributes should create a new form with FormGroup', () => {
        const formGroup = service.createUserAttributesFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            name: expect.any(Object),
            phone: expect.any(Object),
            email: expect.any(Object),
            address: expect.any(Object),
            country: expect.any(Object),
            state: expect.any(Object),
            district: expect.any(Object),
            city: expect.any(Object),
            location: expect.any(Object),
          }),
        );
      });
    });

    describe('getUserAttributes', () => {
      it('should return NewUserAttributes for default UserAttributes initial value', () => {
        const formGroup = service.createUserAttributesFormGroup(sampleWithNewData);

        const userAttributes = service.getUserAttributes(formGroup) as any;

        expect(userAttributes).toMatchObject(sampleWithNewData);
      });

      it('should return NewUserAttributes for empty UserAttributes initial value', () => {
        const formGroup = service.createUserAttributesFormGroup();

        const userAttributes = service.getUserAttributes(formGroup) as any;

        expect(userAttributes).toMatchObject({});
      });

      it('should return IUserAttributes', () => {
        const formGroup = service.createUserAttributesFormGroup(sampleWithRequiredData);

        const userAttributes = service.getUserAttributes(formGroup) as any;

        expect(userAttributes).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IUserAttributes should not enable id FormControl', () => {
        const formGroup = service.createUserAttributesFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewUserAttributes should disable id FormControl', () => {
        const formGroup = service.createUserAttributesFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
