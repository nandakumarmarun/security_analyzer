import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { IApplicationUser } from 'app/entities/application-user/application-user.model';
import { ApplicationUserService } from 'app/entities/application-user/service/application-user.service';
import { SecurityTestService } from '../service/security-test.service';
import { ISecurityTest } from '../security-test.model';
import { SecurityTestFormService } from './security-test-form.service';

import { SecurityTestUpdateComponent } from './security-test-update.component';

describe('SecurityTest Management Update Component', () => {
  let comp: SecurityTestUpdateComponent;
  let fixture: ComponentFixture<SecurityTestUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let securityTestFormService: SecurityTestFormService;
  let securityTestService: SecurityTestService;
  let applicationUserService: ApplicationUserService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([]), SecurityTestUpdateComponent],
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
      .overrideTemplate(SecurityTestUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(SecurityTestUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    securityTestFormService = TestBed.inject(SecurityTestFormService);
    securityTestService = TestBed.inject(SecurityTestService);
    applicationUserService = TestBed.inject(ApplicationUserService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call ApplicationUser query and add missing value', () => {
      const securityTest: ISecurityTest = { id: 456 };
      const applicationUser: IApplicationUser = { id: 32387 };
      securityTest.applicationUser = applicationUser;

      const applicationUserCollection: IApplicationUser[] = [{ id: 4625 }];
      jest.spyOn(applicationUserService, 'query').mockReturnValue(of(new HttpResponse({ body: applicationUserCollection })));
      const additionalApplicationUsers = [applicationUser];
      const expectedCollection: IApplicationUser[] = [...additionalApplicationUsers, ...applicationUserCollection];
      jest.spyOn(applicationUserService, 'addApplicationUserToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ securityTest });
      comp.ngOnInit();

      expect(applicationUserService.query).toHaveBeenCalled();
      expect(applicationUserService.addApplicationUserToCollectionIfMissing).toHaveBeenCalledWith(
        applicationUserCollection,
        ...additionalApplicationUsers.map(expect.objectContaining),
      );
      expect(comp.applicationUsersSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const securityTest: ISecurityTest = { id: 456 };
      const applicationUser: IApplicationUser = { id: 5168 };
      securityTest.applicationUser = applicationUser;

      activatedRoute.data = of({ securityTest });
      comp.ngOnInit();

      expect(comp.applicationUsersSharedCollection).toContain(applicationUser);
      expect(comp.securityTest).toEqual(securityTest);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ISecurityTest>>();
      const securityTest = { id: 123 };
      jest.spyOn(securityTestFormService, 'getSecurityTest').mockReturnValue(securityTest);
      jest.spyOn(securityTestService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ securityTest });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: securityTest }));
      saveSubject.complete();

      // THEN
      expect(securityTestFormService.getSecurityTest).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(securityTestService.update).toHaveBeenCalledWith(expect.objectContaining(securityTest));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ISecurityTest>>();
      const securityTest = { id: 123 };
      jest.spyOn(securityTestFormService, 'getSecurityTest').mockReturnValue({ id: null });
      jest.spyOn(securityTestService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ securityTest: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: securityTest }));
      saveSubject.complete();

      // THEN
      expect(securityTestFormService.getSecurityTest).toHaveBeenCalled();
      expect(securityTestService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ISecurityTest>>();
      const securityTest = { id: 123 };
      jest.spyOn(securityTestService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ securityTest });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(securityTestService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('compareApplicationUser', () => {
      it('Should forward to applicationUserService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(applicationUserService, 'compareApplicationUser');
        comp.compareApplicationUser(entity, entity2);
        expect(applicationUserService.compareApplicationUser).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
