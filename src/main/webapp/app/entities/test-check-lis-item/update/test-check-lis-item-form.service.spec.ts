import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../test-check-lis-item.test-samples';

import { TestCheckLisItemFormService } from './test-check-lis-item-form.service';

describe('TestCheckLisItem Form Service', () => {
  let service: TestCheckLisItemFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(TestCheckLisItemFormService);
  });

  describe('Service methods', () => {
    describe('createTestCheckLisItemFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createTestCheckLisItemFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            marked: expect.any(Object),
            checklistitem: expect.any(Object),
            testCheckList: expect.any(Object),
          }),
        );
      });

      it('passing ITestCheckLisItem should create a new form with FormGroup', () => {
        const formGroup = service.createTestCheckLisItemFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            marked: expect.any(Object),
            checklistitem: expect.any(Object),
            testCheckList: expect.any(Object),
          }),
        );
      });
    });

    describe('getTestCheckLisItem', () => {
      it('should return NewTestCheckLisItem for default TestCheckLisItem initial value', () => {
        const formGroup = service.createTestCheckLisItemFormGroup(sampleWithNewData);

        const testCheckLisItem = service.getTestCheckLisItem(formGroup) as any;

        expect(testCheckLisItem).toMatchObject(sampleWithNewData);
      });

      it('should return NewTestCheckLisItem for empty TestCheckLisItem initial value', () => {
        const formGroup = service.createTestCheckLisItemFormGroup();

        const testCheckLisItem = service.getTestCheckLisItem(formGroup) as any;

        expect(testCheckLisItem).toMatchObject({});
      });

      it('should return ITestCheckLisItem', () => {
        const formGroup = service.createTestCheckLisItemFormGroup(sampleWithRequiredData);

        const testCheckLisItem = service.getTestCheckLisItem(formGroup) as any;

        expect(testCheckLisItem).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing ITestCheckLisItem should not enable id FormControl', () => {
        const formGroup = service.createTestCheckLisItemFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewTestCheckLisItem should disable id FormControl', () => {
        const formGroup = service.createTestCheckLisItemFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
