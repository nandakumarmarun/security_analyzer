import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { ICountry } from 'app/entities/country/country.model';
import { CountryService } from 'app/entities/country/service/country.service';
import { CityService } from '../service/city.service';
import { ICity } from '../city.model';
import { CityFormService } from './city-form.service';

import { CityUpdateComponent } from './city-update.component';

describe('City Management Update Component', () => {
  let comp: CityUpdateComponent;
  let fixture: ComponentFixture<CityUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let cityFormService: CityFormService;
  let cityService: CityService;
  let countryService: CountryService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([]), CityUpdateComponent],
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
      .overrideTemplate(CityUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(CityUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    cityFormService = TestBed.inject(CityFormService);
    cityService = TestBed.inject(CityService);
    countryService = TestBed.inject(CountryService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call Country query and add missing value', () => {
      const city: ICity = { id: 456 };
      const country: ICountry = { id: 21480 };
      city.country = country;

      const countryCollection: ICountry[] = [{ id: 1463 }];
      jest.spyOn(countryService, 'query').mockReturnValue(of(new HttpResponse({ body: countryCollection })));
      const additionalCountries = [country];
      const expectedCollection: ICountry[] = [...additionalCountries, ...countryCollection];
      jest.spyOn(countryService, 'addCountryToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ city });
      comp.ngOnInit();

      expect(countryService.query).toHaveBeenCalled();
      expect(countryService.addCountryToCollectionIfMissing).toHaveBeenCalledWith(
        countryCollection,
        ...additionalCountries.map(expect.objectContaining),
      );
      expect(comp.countriesSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const city: ICity = { id: 456 };
      const country: ICountry = { id: 2834 };
      city.country = country;

      activatedRoute.data = of({ city });
      comp.ngOnInit();

      expect(comp.countriesSharedCollection).toContain(country);
      expect(comp.city).toEqual(city);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ICity>>();
      const city = { id: 123 };
      jest.spyOn(cityFormService, 'getCity').mockReturnValue(city);
      jest.spyOn(cityService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ city });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: city }));
      saveSubject.complete();

      // THEN
      expect(cityFormService.getCity).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(cityService.update).toHaveBeenCalledWith(expect.objectContaining(city));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ICity>>();
      const city = { id: 123 };
      jest.spyOn(cityFormService, 'getCity').mockReturnValue({ id: null });
      jest.spyOn(cityService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ city: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: city }));
      saveSubject.complete();

      // THEN
      expect(cityFormService.getCity).toHaveBeenCalled();
      expect(cityService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ICity>>();
      const city = { id: 123 };
      jest.spyOn(cityService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ city });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(cityService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('compareCountry', () => {
      it('Should forward to countryService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(countryService, 'compareCountry');
        comp.compareCountry(entity, entity2);
        expect(countryService.compareCountry).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
