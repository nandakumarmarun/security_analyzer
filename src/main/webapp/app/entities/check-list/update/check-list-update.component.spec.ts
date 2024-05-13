import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { CheckListService } from '../service/check-list.service';
import { ICheckList } from '../check-list.model';
import { CheckListFormService } from './check-list-form.service';

import { CheckListUpdateComponent } from './check-list-update.component';

describe('CheckList Management Update Component', () => {
  let comp: CheckListUpdateComponent;
  let fixture: ComponentFixture<CheckListUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let checkListFormService: CheckListFormService;
  let checkListService: CheckListService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([]), CheckListUpdateComponent],
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
      .overrideTemplate(CheckListUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(CheckListUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    checkListFormService = TestBed.inject(CheckListFormService);
    checkListService = TestBed.inject(CheckListService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should update editForm', () => {
      const checkList: ICheckList = { id: 456 };

      activatedRoute.data = of({ checkList });
      comp.ngOnInit();

      expect(comp.checkList).toEqual(checkList);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ICheckList>>();
      const checkList = { id: 123 };
      jest.spyOn(checkListFormService, 'getCheckList').mockReturnValue(checkList);
      jest.spyOn(checkListService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ checkList });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: checkList }));
      saveSubject.complete();

      // THEN
      expect(checkListFormService.getCheckList).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(checkListService.update).toHaveBeenCalledWith(expect.objectContaining(checkList));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ICheckList>>();
      const checkList = { id: 123 };
      jest.spyOn(checkListFormService, 'getCheckList').mockReturnValue({ id: null });
      jest.spyOn(checkListService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ checkList: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: checkList }));
      saveSubject.complete();

      // THEN
      expect(checkListFormService.getCheckList).toHaveBeenCalled();
      expect(checkListService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ICheckList>>();
      const checkList = { id: 123 };
      jest.spyOn(checkListService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ checkList });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(checkListService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
