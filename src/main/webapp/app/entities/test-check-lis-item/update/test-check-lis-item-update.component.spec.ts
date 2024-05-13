import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { ICheckLisItem } from 'app/entities/check-lis-item/check-lis-item.model';
import { CheckLisItemService } from 'app/entities/check-lis-item/service/check-lis-item.service';
import { ITestCheckList } from 'app/entities/test-check-list/test-check-list.model';
import { TestCheckListService } from 'app/entities/test-check-list/service/test-check-list.service';
import { ITestCheckLisItem } from '../test-check-lis-item.model';
import { TestCheckLisItemService } from '../service/test-check-lis-item.service';
import { TestCheckLisItemFormService } from './test-check-lis-item-form.service';

import { TestCheckLisItemUpdateComponent } from './test-check-lis-item-update.component';

describe('TestCheckLisItem Management Update Component', () => {
  let comp: TestCheckLisItemUpdateComponent;
  let fixture: ComponentFixture<TestCheckLisItemUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let testCheckLisItemFormService: TestCheckLisItemFormService;
  let testCheckLisItemService: TestCheckLisItemService;
  let checkLisItemService: CheckLisItemService;
  let testCheckListService: TestCheckListService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([]), TestCheckLisItemUpdateComponent],
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
      .overrideTemplate(TestCheckLisItemUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(TestCheckLisItemUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    testCheckLisItemFormService = TestBed.inject(TestCheckLisItemFormService);
    testCheckLisItemService = TestBed.inject(TestCheckLisItemService);
    checkLisItemService = TestBed.inject(CheckLisItemService);
    testCheckListService = TestBed.inject(TestCheckListService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call checklistitem query and add missing value', () => {
      const testCheckLisItem: ITestCheckLisItem = { id: 456 };
      const checklistitem: ICheckLisItem = { id: 25152 };
      testCheckLisItem.checklistitem = checklistitem;

      const checklistitemCollection: ICheckLisItem[] = [{ id: 4838 }];
      jest.spyOn(checkLisItemService, 'query').mockReturnValue(of(new HttpResponse({ body: checklistitemCollection })));
      const expectedCollection: ICheckLisItem[] = [checklistitem, ...checklistitemCollection];
      jest.spyOn(checkLisItemService, 'addCheckLisItemToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ testCheckLisItem });
      comp.ngOnInit();

      expect(checkLisItemService.query).toHaveBeenCalled();
      expect(checkLisItemService.addCheckLisItemToCollectionIfMissing).toHaveBeenCalledWith(checklistitemCollection, checklistitem);
      expect(comp.checklistitemsCollection).toEqual(expectedCollection);
    });

    it('Should call TestCheckList query and add missing value', () => {
      const testCheckLisItem: ITestCheckLisItem = { id: 456 };
      const testCheckList: ITestCheckList = { id: 8037 };
      testCheckLisItem.testCheckList = testCheckList;

      const testCheckListCollection: ITestCheckList[] = [{ id: 28621 }];
      jest.spyOn(testCheckListService, 'query').mockReturnValue(of(new HttpResponse({ body: testCheckListCollection })));
      const additionalTestCheckLists = [testCheckList];
      const expectedCollection: ITestCheckList[] = [...additionalTestCheckLists, ...testCheckListCollection];
      jest.spyOn(testCheckListService, 'addTestCheckListToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ testCheckLisItem });
      comp.ngOnInit();

      expect(testCheckListService.query).toHaveBeenCalled();
      expect(testCheckListService.addTestCheckListToCollectionIfMissing).toHaveBeenCalledWith(
        testCheckListCollection,
        ...additionalTestCheckLists.map(expect.objectContaining),
      );
      expect(comp.testCheckListsSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const testCheckLisItem: ITestCheckLisItem = { id: 456 };
      const checklistitem: ICheckLisItem = { id: 8814 };
      testCheckLisItem.checklistitem = checklistitem;
      const testCheckList: ITestCheckList = { id: 28050 };
      testCheckLisItem.testCheckList = testCheckList;

      activatedRoute.data = of({ testCheckLisItem });
      comp.ngOnInit();

      expect(comp.checklistitemsCollection).toContain(checklistitem);
      expect(comp.testCheckListsSharedCollection).toContain(testCheckList);
      expect(comp.testCheckLisItem).toEqual(testCheckLisItem);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ITestCheckLisItem>>();
      const testCheckLisItem = { id: 123 };
      jest.spyOn(testCheckLisItemFormService, 'getTestCheckLisItem').mockReturnValue(testCheckLisItem);
      jest.spyOn(testCheckLisItemService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ testCheckLisItem });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: testCheckLisItem }));
      saveSubject.complete();

      // THEN
      expect(testCheckLisItemFormService.getTestCheckLisItem).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(testCheckLisItemService.update).toHaveBeenCalledWith(expect.objectContaining(testCheckLisItem));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ITestCheckLisItem>>();
      const testCheckLisItem = { id: 123 };
      jest.spyOn(testCheckLisItemFormService, 'getTestCheckLisItem').mockReturnValue({ id: null });
      jest.spyOn(testCheckLisItemService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ testCheckLisItem: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: testCheckLisItem }));
      saveSubject.complete();

      // THEN
      expect(testCheckLisItemFormService.getTestCheckLisItem).toHaveBeenCalled();
      expect(testCheckLisItemService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ITestCheckLisItem>>();
      const testCheckLisItem = { id: 123 };
      jest.spyOn(testCheckLisItemService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ testCheckLisItem });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(testCheckLisItemService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('compareCheckLisItem', () => {
      it('Should forward to checkLisItemService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(checkLisItemService, 'compareCheckLisItem');
        comp.compareCheckLisItem(entity, entity2);
        expect(checkLisItemService.compareCheckLisItem).toHaveBeenCalledWith(entity, entity2);
      });
    });

    describe('compareTestCheckList', () => {
      it('Should forward to testCheckListService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(testCheckListService, 'compareTestCheckList');
        comp.compareTestCheckList(entity, entity2);
        expect(testCheckListService.compareTestCheckList).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
