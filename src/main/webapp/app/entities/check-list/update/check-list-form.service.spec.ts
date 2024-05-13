import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../check-list.test-samples';

import { CheckListFormService } from './check-list-form.service';

describe('CheckList Form Service', () => {
  let service: CheckListFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(CheckListFormService);
  });

  describe('Service methods', () => {
    describe('createCheckListFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createCheckListFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            name: expect.any(Object),
          }),
        );
      });

      it('passing ICheckList should create a new form with FormGroup', () => {
        const formGroup = service.createCheckListFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            name: expect.any(Object),
          }),
        );
      });
    });

    describe('getCheckList', () => {
      it('should return NewCheckList for default CheckList initial value', () => {
        const formGroup = service.createCheckListFormGroup(sampleWithNewData);

        const checkList = service.getCheckList(formGroup) as any;

        expect(checkList).toMatchObject(sampleWithNewData);
      });

      it('should return NewCheckList for empty CheckList initial value', () => {
        const formGroup = service.createCheckListFormGroup();

        const checkList = service.getCheckList(formGroup) as any;

        expect(checkList).toMatchObject({});
      });

      it('should return ICheckList', () => {
        const formGroup = service.createCheckListFormGroup(sampleWithRequiredData);

        const checkList = service.getCheckList(formGroup) as any;

        expect(checkList).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing ICheckList should not enable id FormControl', () => {
        const formGroup = service.createCheckListFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewCheckList should disable id FormControl', () => {
        const formGroup = service.createCheckListFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
