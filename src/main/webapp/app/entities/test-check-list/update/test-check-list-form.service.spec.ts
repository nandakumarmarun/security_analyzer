import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../test-check-list.test-samples';

import { TestCheckListFormService } from './test-check-list-form.service';

describe('TestCheckList Form Service', () => {
  let service: TestCheckListFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(TestCheckListFormService);
  });

  describe('Service methods', () => {
    describe('createTestCheckListFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createTestCheckListFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            checkList: expect.any(Object),
            securityTest: expect.any(Object),
          }),
        );
      });

      it('passing ITestCheckList should create a new form with FormGroup', () => {
        const formGroup = service.createTestCheckListFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            checkList: expect.any(Object),
            securityTest: expect.any(Object),
          }),
        );
      });
    });

    describe('getTestCheckList', () => {
      it('should return NewTestCheckList for default TestCheckList initial value', () => {
        const formGroup = service.createTestCheckListFormGroup(sampleWithNewData);

        const testCheckList = service.getTestCheckList(formGroup) as any;

        expect(testCheckList).toMatchObject(sampleWithNewData);
      });

      it('should return NewTestCheckList for empty TestCheckList initial value', () => {
        const formGroup = service.createTestCheckListFormGroup();

        const testCheckList = service.getTestCheckList(formGroup) as any;

        expect(testCheckList).toMatchObject({});
      });

      it('should return ITestCheckList', () => {
        const formGroup = service.createTestCheckListFormGroup(sampleWithRequiredData);

        const testCheckList = service.getTestCheckList(formGroup) as any;

        expect(testCheckList).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing ITestCheckList should not enable id FormControl', () => {
        const formGroup = service.createTestCheckListFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewTestCheckList should disable id FormControl', () => {
        const formGroup = service.createTestCheckListFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
