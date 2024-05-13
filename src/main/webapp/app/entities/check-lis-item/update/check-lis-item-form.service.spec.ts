import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../check-lis-item.test-samples';

import { CheckLisItemFormService } from './check-lis-item-form.service';

describe('CheckLisItem Form Service', () => {
  let service: CheckLisItemFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(CheckLisItemFormService);
  });

  describe('Service methods', () => {
    describe('createCheckLisItemFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createCheckLisItemFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            name: expect.any(Object),
            value: expect.any(Object),
            checkList: expect.any(Object),
          }),
        );
      });

      it('passing ICheckLisItem should create a new form with FormGroup', () => {
        const formGroup = service.createCheckLisItemFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            name: expect.any(Object),
            value: expect.any(Object),
            checkList: expect.any(Object),
          }),
        );
      });
    });

    describe('getCheckLisItem', () => {
      it('should return NewCheckLisItem for default CheckLisItem initial value', () => {
        const formGroup = service.createCheckLisItemFormGroup(sampleWithNewData);

        const checkLisItem = service.getCheckLisItem(formGroup) as any;

        expect(checkLisItem).toMatchObject(sampleWithNewData);
      });

      it('should return NewCheckLisItem for empty CheckLisItem initial value', () => {
        const formGroup = service.createCheckLisItemFormGroup();

        const checkLisItem = service.getCheckLisItem(formGroup) as any;

        expect(checkLisItem).toMatchObject({});
      });

      it('should return ICheckLisItem', () => {
        const formGroup = service.createCheckLisItemFormGroup(sampleWithRequiredData);

        const checkLisItem = service.getCheckLisItem(formGroup) as any;

        expect(checkLisItem).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing ICheckLisItem should not enable id FormControl', () => {
        const formGroup = service.createCheckLisItemFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewCheckLisItem should disable id FormControl', () => {
        const formGroup = service.createCheckLisItemFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
