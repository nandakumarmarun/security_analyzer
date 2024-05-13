import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { ICheckList } from 'app/entities/check-list/check-list.model';
import { CheckListService } from 'app/entities/check-list/service/check-list.service';
import { ISecurityTest } from 'app/entities/security-test/security-test.model';
import { SecurityTestService } from 'app/entities/security-test/service/security-test.service';
import { ITestCheckList } from '../test-check-list.model';
import { TestCheckListService } from '../service/test-check-list.service';
import { TestCheckListFormService } from './test-check-list-form.service';

import { TestCheckListUpdateComponent } from './test-check-list-update.component';

describe('TestCheckList Management Update Component', () => {
  let comp: TestCheckListUpdateComponent;
  let fixture: ComponentFixture<TestCheckListUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let testCheckListFormService: TestCheckListFormService;
  let testCheckListService: TestCheckListService;
  let checkListService: CheckListService;
  let securityTestService: SecurityTestService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([]), TestCheckListUpdateComponent],
      providers: [
        FormBuilder,
        {
          provide: ActivatedRoute,
          useValue: {
            params: from([{}]),
          },
        },
      ],
    })
      .overrideTemplate(TestCheckListUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(TestCheckListUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    testCheckListFormService = TestBed.inject(TestCheckListFormService);
    testCheckListService = TestBed.inject(TestCheckListService);
    checkListService = TestBed.inject(CheckListService);
    securityTestService = TestBed.inject(SecurityTestService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call checkList query and add missing value', () => {
      const testCheckList: ITestCheckList = { id: 456 };
      const checkList: ICheckList = { id: 10631 };
      testCheckList.checkList = checkList;

      const checkListCollection: ICheckList[] = [{ id: 25088 }];
      jest.spyOn(checkListService, 'query').mockReturnValue(of(new HttpResponse({ body: checkListCollection })));
      const expectedCollection: ICheckList[] = [checkList, ...checkListCollection];
      jest.spyOn(checkListService, 'addCheckListToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ testCheckList });
      comp.ngOnInit();

      expect(checkListService.query).toHaveBeenCalled();
      expect(checkListService.addCheckListToCollectionIfMissing).toHaveBeenCalledWith(checkListCollection, checkList);
      expect(comp.checkListsCollection).toEqual(expectedCollection);
    });

    it('Should call SecurityTest query and add missing value', () => {
      const testCheckList: ITestCheckList = { id: 456 };
      const securityTest: ISecurityTest = { id: 106 };
      testCheckList.securityTest = securityTest;

      const securityTestCollection: ISecurityTest[] = [{ id: 27362 }];
      jest.spyOn(securityTestService, 'query').mockReturnValue(of(new HttpResponse({ body: securityTestCollection })));
      const additionalSecurityTests = [securityTest];
      const expectedCollection: ISecurityTest[] = [...additionalSecurityTests, ...securityTestCollection];
      jest.spyOn(securityTestService, 'addSecurityTestToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ testCheckList });
      comp.ngOnInit();

      expect(securityTestService.query).toHaveBeenCalled();
      expect(securityTestService.addSecurityTestToCollectionIfMissing).toHaveBeenCalledWith(
        securityTestCollection,
        ...additionalSecurityTests.map(expect.objectContaining),
      );
      expect(comp.securityTestsSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const testCheckList: ITestCheckList = { id: 456 };
      const checkList: ICheckList = { id: 30803 };
      testCheckList.checkList = checkList;
      const securityTest: ISecurityTest = { id: 16061 };
      testCheckList.securityTest = securityTest;

      activatedRoute.data = of({ testCheckList });
      comp.ngOnInit();

      expect(comp.checkListsCollection).toContain(checkList);
      expect(comp.securityTestsSharedCollection).toContain(securityTest);
      expect(comp.testCheckList).toEqual(testCheckList);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ITestCheckList>>();
      const testCheckList = { id: 123 };
      jest.spyOn(testCheckListFormService, 'getTestCheckList').mockReturnValue(testCheckList);
      jest.spyOn(testCheckListService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ testCheckList });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: testCheckList }));
      saveSubject.complete();

      // THEN
      expect(testCheckListFormService.getTestCheckList).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(testCheckListService.update).toHaveBeenCalledWith(expect.objectContaining(testCheckList));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ITestCheckList>>();
      const testCheckList = { id: 123 };
      jest.spyOn(testCheckListFormService, 'getTestCheckList').mockReturnValue({ id: null });
      jest.spyOn(testCheckListService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ testCheckList: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: testCheckList }));
      saveSubject.complete();

      // THEN
      expect(testCheckListFormService.getTestCheckList).toHaveBeenCalled();
      expect(testCheckListService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ITestCheckList>>();
      const testCheckList = { id: 123 };
      jest.spyOn(testCheckListService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ testCheckList });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(testCheckListService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('compareCheckList', () => {
      it('Should forward to checkListService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(checkListService, 'compareCheckList');
        comp.compareCheckList(entity, entity2);
        expect(checkListService.compareCheckList).toHaveBeenCalledWith(entity, entity2);
      });
    });

    describe('compareSecurityTest', () => {
      it('Should forward to securityTestService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(securityTestService, 'compareSecurityTest');
        comp.compareSecurityTest(entity, entity2);
        expect(securityTestService.compareSecurityTest).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
