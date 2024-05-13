import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { ICheckList } from 'app/entities/check-list/check-list.model';
import { CheckListService } from 'app/entities/check-list/service/check-list.service';
import { CheckLisItemService } from '../service/check-lis-item.service';
import { ICheckLisItem } from '../check-lis-item.model';
import { CheckLisItemFormService } from './check-lis-item-form.service';

import { CheckLisItemUpdateComponent } from './check-lis-item-update.component';

describe('CheckLisItem Management Update Component', () => {
  let comp: CheckLisItemUpdateComponent;
  let fixture: ComponentFixture<CheckLisItemUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let checkLisItemFormService: CheckLisItemFormService;
  let checkLisItemService: CheckLisItemService;
  let checkListService: CheckListService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([]), CheckLisItemUpdateComponent],
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
      .overrideTemplate(CheckLisItemUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(CheckLisItemUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    checkLisItemFormService = TestBed.inject(CheckLisItemFormService);
    checkLisItemService = TestBed.inject(CheckLisItemService);
    checkListService = TestBed.inject(CheckListService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call CheckList query and add missing value', () => {
      const checkLisItem: ICheckLisItem = { id: 456 };
      const checkList: ICheckList = { id: 28294 };
      checkLisItem.checkList = checkList;

      const checkListCollection: ICheckList[] = [{ id: 7914 }];
      jest.spyOn(checkListService, 'query').mockReturnValue(of(new HttpResponse({ body: checkListCollection })));
      const additionalCheckLists = [checkList];
      const expectedCollection: ICheckList[] = [...additionalCheckLists, ...checkListCollection];
      jest.spyOn(checkListService, 'addCheckListToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ checkLisItem });
      comp.ngOnInit();

      expect(checkListService.query).toHaveBeenCalled();
      expect(checkListService.addCheckListToCollectionIfMissing).toHaveBeenCalledWith(
        checkListCollection,
        ...additionalCheckLists.map(expect.objectContaining),
      );
      expect(comp.checkListsSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const checkLisItem: ICheckLisItem = { id: 456 };
      const checkList: ICheckList = { id: 30824 };
      checkLisItem.checkList = checkList;

      activatedRoute.data = of({ checkLisItem });
      comp.ngOnInit();

      expect(comp.checkListsSharedCollection).toContain(checkList);
      expect(comp.checkLisItem).toEqual(checkLisItem);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ICheckLisItem>>();
      const checkLisItem = { id: 123 };
      jest.spyOn(checkLisItemFormService, 'getCheckLisItem').mockReturnValue(checkLisItem);
      jest.spyOn(checkLisItemService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ checkLisItem });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: checkLisItem }));
      saveSubject.complete();

      // THEN
      expect(checkLisItemFormService.getCheckLisItem).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(checkLisItemService.update).toHaveBeenCalledWith(expect.objectContaining(checkLisItem));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ICheckLisItem>>();
      const checkLisItem = { id: 123 };
      jest.spyOn(checkLisItemFormService, 'getCheckLisItem').mockReturnValue({ id: null });
      jest.spyOn(checkLisItemService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ checkLisItem: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: checkLisItem }));
      saveSubject.complete();

      // THEN
      expect(checkLisItemFormService.getCheckLisItem).toHaveBeenCalled();
      expect(checkLisItemService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ICheckLisItem>>();
      const checkLisItem = { id: 123 };
      jest.spyOn(checkLisItemService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ checkLisItem });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(checkLisItemService.update).toHaveBeenCalled();
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
  });
});
