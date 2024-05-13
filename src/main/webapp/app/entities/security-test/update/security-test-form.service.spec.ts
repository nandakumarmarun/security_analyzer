import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../security-test.test-samples';

import { SecurityTestFormService } from './security-test-form.service';

describe('SecurityTest Form Service', () => {
  let service: SecurityTestFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(SecurityTestFormService);
  });

  describe('Service methods', () => {
    describe('createSecurityTestFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createSecurityTestFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            testStatus: expect.any(Object),
            testScore: expect.any(Object),
            securityLevel: expect.any(Object),
            applicationUser: expect.any(Object),
          }),
        );
      });

      it('passing ISecurityTest should create a new form with FormGroup', () => {
        const formGroup = service.createSecurityTestFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            testStatus: expect.any(Object),
            testScore: expect.any(Object),
            securityLevel: expect.any(Object),
            applicationUser: expect.any(Object),
          }),
        );
      });
    });

    describe('getSecurityTest', () => {
      it('should return NewSecurityTest for default SecurityTest initial value', () => {
        const formGroup = service.createSecurityTestFormGroup(sampleWithNewData);

        const securityTest = service.getSecurityTest(formGroup) as any;

        expect(securityTest).toMatchObject(sampleWithNewData);
      });

      it('should return NewSecurityTest for empty SecurityTest initial value', () => {
        const formGroup = service.createSecurityTestFormGroup();

        const securityTest = service.getSecurityTest(formGroup) as any;

        expect(securityTest).toMatchObject({});
      });

      it('should return ISecurityTest', () => {
        const formGroup = service.createSecurityTestFormGroup(sampleWithRequiredData);

        const securityTest = service.getSecurityTest(formGroup) as any;

        expect(securityTest).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing ISecurityTest should not enable id FormControl', () => {
        const formGroup = service.createSecurityTestFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewSecurityTest should disable id FormControl', () => {
        const formGroup = service.createSecurityTestFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
