import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { ICountry } from 'app/entities/country/country.model';
import { CountryService } from 'app/entities/country/service/country.service';
import { IState } from 'app/entities/state/state.model';
import { StateService } from 'app/entities/state/service/state.service';
import { IDistrict } from 'app/entities/district/district.model';
import { DistrictService } from 'app/entities/district/service/district.service';
import { ICity } from 'app/entities/city/city.model';
import { CityService } from 'app/entities/city/service/city.service';
import { ILocation } from 'app/entities/location/location.model';
import { LocationService } from 'app/entities/location/service/location.service';
import { IUserAttributes } from '../user-attributes.model';
import { UserAttributesService } from '../service/user-attributes.service';
import { UserAttributesFormService } from './user-attributes-form.service';

import { UserAttributesUpdateComponent } from './user-attributes-update.component';

describe('UserAttributes Management Update Component', () => {
  let comp: UserAttributesUpdateComponent;
  let fixture: ComponentFixture<UserAttributesUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let userAttributesFormService: UserAttributesFormService;
  let userAttributesService: UserAttributesService;
  let countryService: CountryService;
  let stateService: StateService;
  let districtService: DistrictService;
  let cityService: CityService;
  let locationService: LocationService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([]), UserAttributesUpdateComponent],
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
      .overrideTemplate(UserAttributesUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(UserAttributesUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    userAttributesFormService = TestBed.inject(UserAttributesFormService);
    userAttributesService = TestBed.inject(UserAttributesService);
    countryService = TestBed.inject(CountryService);
    stateService = TestBed.inject(StateService);
    districtService = TestBed.inject(DistrictService);
    cityService = TestBed.inject(CityService);
    locationService = TestBed.inject(LocationService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call country query and add missing value', () => {
      const userAttributes: IUserAttributes = { id: 456 };
      const country: ICountry = { id: 14039 };
      userAttributes.country = country;

      const countryCollection: ICountry[] = [{ id: 324 }];
      jest.spyOn(countryService, 'query').mockReturnValue(of(new HttpResponse({ body: countryCollection })));
      const expectedCollection: ICountry[] = [country, ...countryCollection];
      jest.spyOn(countryService, 'addCountryToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ userAttributes });
      comp.ngOnInit();

      expect(countryService.query).toHaveBeenCalled();
      expect(countryService.addCountryToCollectionIfMissing).toHaveBeenCalledWith(countryCollection, country);
      expect(comp.countriesCollection).toEqual(expectedCollection);
    });

    it('Should call state query and add missing value', () => {
      const userAttributes: IUserAttributes = { id: 456 };
      const state: IState = { id: 11495 };
      userAttributes.state = state;

      const stateCollection: IState[] = [{ id: 496 }];
      jest.spyOn(stateService, 'query').mockReturnValue(of(new HttpResponse({ body: stateCollection })));
      const expectedCollection: IState[] = [state, ...stateCollection];
      jest.spyOn(stateService, 'addStateToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ userAttributes });
      comp.ngOnInit();

      expect(stateService.query).toHaveBeenCalled();
      expect(stateService.addStateToCollectionIfMissing).toHaveBeenCalledWith(stateCollection, state);
      expect(comp.statesCollection).toEqual(expectedCollection);
    });

    it('Should call district query and add missing value', () => {
      const userAttributes: IUserAttributes = { id: 456 };
      const district: IDistrict = { id: 2295 };
      userAttributes.district = district;

      const districtCollection: IDistrict[] = [{ id: 13567 }];
      jest.spyOn(districtService, 'query').mockReturnValue(of(new HttpResponse({ body: districtCollection })));
      const expectedCollection: IDistrict[] = [district, ...districtCollection];
      jest.spyOn(districtService, 'addDistrictToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ userAttributes });
      comp.ngOnInit();

      expect(districtService.query).toHaveBeenCalled();
      expect(districtService.addDistrictToCollectionIfMissing).toHaveBeenCalledWith(districtCollection, district);
      expect(comp.districtsCollection).toEqual(expectedCollection);
    });

    it('Should call city query and add missing value', () => {
      const userAttributes: IUserAttributes = { id: 456 };
      const city: ICity = { id: 30397 };
      userAttributes.city = city;

      const cityCollection: ICity[] = [{ id: 2614 }];
      jest.spyOn(cityService, 'query').mockReturnValue(of(new HttpResponse({ body: cityCollection })));
      const expectedCollection: ICity[] = [city, ...cityCollection];
      jest.spyOn(cityService, 'addCityToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ userAttributes });
      comp.ngOnInit();

      expect(cityService.query).toHaveBeenCalled();
      expect(cityService.addCityToCollectionIfMissing).toHaveBeenCalledWith(cityCollection, city);
      expect(comp.citiesCollection).toEqual(expectedCollection);
    });

    it('Should call location query and add missing value', () => {
      const userAttributes: IUserAttributes = { id: 456 };
      const location: ILocation = { id: 12081 };
      userAttributes.location = location;

      const locationCollection: ILocation[] = [{ id: 2595 }];
      jest.spyOn(locationService, 'query').mockReturnValue(of(new HttpResponse({ body: locationCollection })));
      const expectedCollection: ILocation[] = [location, ...locationCollection];
      jest.spyOn(locationService, 'addLocationToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ userAttributes });
      comp.ngOnInit();

      expect(locationService.query).toHaveBeenCalled();
      expect(locationService.addLocationToCollectionIfMissing).toHaveBeenCalledWith(locationCollection, location);
      expect(comp.locationsCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const userAttributes: IUserAttributes = { id: 456 };
      const country: ICountry = { id: 13206 };
      userAttributes.country = country;
      const state: IState = { id: 5842 };
      userAttributes.state = state;
      const district: IDistrict = { id: 22809 };
      userAttributes.district = district;
      const city: ICity = { id: 3308 };
      userAttributes.city = city;
      const location: ILocation = { id: 27104 };
      userAttributes.location = location;

      activatedRoute.data = of({ userAttributes });
      comp.ngOnInit();

      expect(comp.countriesCollection).toContain(country);
      expect(comp.statesCollection).toContain(state);
      expect(comp.districtsCollection).toContain(district);
      expect(comp.citiesCollection).toContain(city);
      expect(comp.locationsCollection).toContain(location);
      expect(comp.userAttributes).toEqual(userAttributes);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IUserAttributes>>();
      const userAttributes = { id: 123 };
      jest.spyOn(userAttributesFormService, 'getUserAttributes').mockReturnValue(userAttributes);
      jest.spyOn(userAttributesService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ userAttributes });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: userAttributes }));
      saveSubject.complete();

      // THEN
      expect(userAttributesFormService.getUserAttributes).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(userAttributesService.update).toHaveBeenCalledWith(expect.objectContaining(userAttributes));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IUserAttributes>>();
      const userAttributes = { id: 123 };
      jest.spyOn(userAttributesFormService, 'getUserAttributes').mockReturnValue({ id: null });
      jest.spyOn(userAttributesService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ userAttributes: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: userAttributes }));
      saveSubject.complete();

      // THEN
      expect(userAttributesFormService.getUserAttributes).toHaveBeenCalled();
      expect(userAttributesService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IUserAttributes>>();
      const userAttributes = { id: 123 };
      jest.spyOn(userAttributesService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ userAttributes });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(userAttributesService.update).toHaveBeenCalled();
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

    describe('compareState', () => {
      it('Should forward to stateService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(stateService, 'compareState');
        comp.compareState(entity, entity2);
        expect(stateService.compareState).toHaveBeenCalledWith(entity, entity2);
      });
    });

    describe('compareDistrict', () => {
      it('Should forward to districtService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(districtService, 'compareDistrict');
        comp.compareDistrict(entity, entity2);
        expect(districtService.compareDistrict).toHaveBeenCalledWith(entity, entity2);
      });
    });

    describe('compareCity', () => {
      it('Should forward to cityService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(cityService, 'compareCity');
        comp.compareCity(entity, entity2);
        expect(cityService.compareCity).toHaveBeenCalledWith(entity, entity2);
      });
    });

    describe('compareLocation', () => {
      it('Should forward to locationService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(locationService, 'compareLocation');
        comp.compareLocation(entity, entity2);
        expect(locationService.compareLocation).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
