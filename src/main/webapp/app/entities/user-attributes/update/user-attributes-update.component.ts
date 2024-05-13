import { Component, inject, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

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
import { UserAttributesService } from '../service/user-attributes.service';
import { IUserAttributes } from '../user-attributes.model';
import { UserAttributesFormService, UserAttributesFormGroup } from './user-attributes-form.service';

@Component({
  standalone: true,
  selector: 'jhi-user-attributes-update',
  templateUrl: './user-attributes-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class UserAttributesUpdateComponent implements OnInit {
  isSaving = false;
  userAttributes: IUserAttributes | null = null;

  countriesCollection: ICountry[] = [];
  statesCollection: IState[] = [];
  districtsCollection: IDistrict[] = [];
  citiesCollection: ICity[] = [];
  locationsCollection: ILocation[] = [];

  protected userAttributesService = inject(UserAttributesService);
  protected userAttributesFormService = inject(UserAttributesFormService);
  protected countryService = inject(CountryService);
  protected stateService = inject(StateService);
  protected districtService = inject(DistrictService);
  protected cityService = inject(CityService);
  protected locationService = inject(LocationService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: UserAttributesFormGroup = this.userAttributesFormService.createUserAttributesFormGroup();

  compareCountry = (o1: ICountry | null, o2: ICountry | null): boolean => this.countryService.compareCountry(o1, o2);

  compareState = (o1: IState | null, o2: IState | null): boolean => this.stateService.compareState(o1, o2);

  compareDistrict = (o1: IDistrict | null, o2: IDistrict | null): boolean => this.districtService.compareDistrict(o1, o2);

  compareCity = (o1: ICity | null, o2: ICity | null): boolean => this.cityService.compareCity(o1, o2);

  compareLocation = (o1: ILocation | null, o2: ILocation | null): boolean => this.locationService.compareLocation(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ userAttributes }) => {
      this.userAttributes = userAttributes;
      if (userAttributes) {
        this.updateForm(userAttributes);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const userAttributes = this.userAttributesFormService.getUserAttributes(this.editForm);
    if (userAttributes.id !== null) {
      this.subscribeToSaveResponse(this.userAttributesService.update(userAttributes));
    } else {
      this.subscribeToSaveResponse(this.userAttributesService.create(userAttributes));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IUserAttributes>>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe({
      next: () => this.onSaveSuccess(),
      error: () => this.onSaveError(),
    });
  }

  protected onSaveSuccess(): void {
    this.previousState();
  }

  protected onSaveError(): void {
    // Api for inheritance.
  }

  protected onSaveFinalize(): void {
    this.isSaving = false;
  }

  protected updateForm(userAttributes: IUserAttributes): void {
    this.userAttributes = userAttributes;
    this.userAttributesFormService.resetForm(this.editForm, userAttributes);

    this.countriesCollection = this.countryService.addCountryToCollectionIfMissing<ICountry>(
      this.countriesCollection,
      userAttributes.country,
    );
    this.statesCollection = this.stateService.addStateToCollectionIfMissing<IState>(this.statesCollection, userAttributes.state);
    this.districtsCollection = this.districtService.addDistrictToCollectionIfMissing<IDistrict>(
      this.districtsCollection,
      userAttributes.district,
    );
    this.citiesCollection = this.cityService.addCityToCollectionIfMissing<ICity>(this.citiesCollection, userAttributes.city);
    this.locationsCollection = this.locationService.addLocationToCollectionIfMissing<ILocation>(
      this.locationsCollection,
      userAttributes.location,
    );
  }

  protected loadRelationshipsOptions(): void {
    this.countryService
      .query({ filter: 'userattributes-is-null' })
      .pipe(map((res: HttpResponse<ICountry[]>) => res.body ?? []))
      .pipe(
        map((countries: ICountry[]) =>
          this.countryService.addCountryToCollectionIfMissing<ICountry>(countries, this.userAttributes?.country),
        ),
      )
      .subscribe((countries: ICountry[]) => (this.countriesCollection = countries));

    this.stateService
      .query({ filter: 'userattributes-is-null' })
      .pipe(map((res: HttpResponse<IState[]>) => res.body ?? []))
      .pipe(map((states: IState[]) => this.stateService.addStateToCollectionIfMissing<IState>(states, this.userAttributes?.state)))
      .subscribe((states: IState[]) => (this.statesCollection = states));

    this.districtService
      .query({ filter: 'userattributes-is-null' })
      .pipe(map((res: HttpResponse<IDistrict[]>) => res.body ?? []))
      .pipe(
        map((districts: IDistrict[]) =>
          this.districtService.addDistrictToCollectionIfMissing<IDistrict>(districts, this.userAttributes?.district),
        ),
      )
      .subscribe((districts: IDistrict[]) => (this.districtsCollection = districts));

    this.cityService
      .query({ filter: 'userattributes-is-null' })
      .pipe(map((res: HttpResponse<ICity[]>) => res.body ?? []))
      .pipe(map((cities: ICity[]) => this.cityService.addCityToCollectionIfMissing<ICity>(cities, this.userAttributes?.city)))
      .subscribe((cities: ICity[]) => (this.citiesCollection = cities));

    this.locationService
      .query({ filter: 'userattributes-is-null' })
      .pipe(map((res: HttpResponse<ILocation[]>) => res.body ?? []))
      .pipe(
        map((locations: ILocation[]) =>
          this.locationService.addLocationToCollectionIfMissing<ILocation>(locations, this.userAttributes?.location),
        ),
      )
      .subscribe((locations: ILocation[]) => (this.locationsCollection = locations));
  }
}
