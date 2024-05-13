import { Component, inject, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { ICountry } from 'app/entities/country/country.model';
import { CountryService } from 'app/entities/country/service/country.service';
import { IState } from '../state.model';
import { StateService } from '../service/state.service';
import { StateFormService, StateFormGroup } from './state-form.service';

@Component({
  standalone: true,
  selector: 'jhi-state-update',
  templateUrl: './state-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class StateUpdateComponent implements OnInit {
  isSaving = false;
  state: IState | null = null;

  countriesSharedCollection: ICountry[] = [];

  protected stateService = inject(StateService);
  protected stateFormService = inject(StateFormService);
  protected countryService = inject(CountryService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: StateFormGroup = this.stateFormService.createStateFormGroup();

  compareCountry = (o1: ICountry | null, o2: ICountry | null): boolean => this.countryService.compareCountry(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ state }) => {
      this.state = state;
      if (state) {
        this.updateForm(state);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const state = this.stateFormService.getState(this.editForm);
    if (state.id !== null) {
      this.subscribeToSaveResponse(this.stateService.update(state));
    } else {
      this.subscribeToSaveResponse(this.stateService.create(state));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IState>>): void {
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

  protected updateForm(state: IState): void {
    this.state = state;
    this.stateFormService.resetForm(this.editForm, state);

    this.countriesSharedCollection = this.countryService.addCountryToCollectionIfMissing<ICountry>(
      this.countriesSharedCollection,
      state.country,
    );
  }

  protected loadRelationshipsOptions(): void {
    this.countryService
      .query()
      .pipe(map((res: HttpResponse<ICountry[]>) => res.body ?? []))
      .pipe(map((countries: ICountry[]) => this.countryService.addCountryToCollectionIfMissing<ICountry>(countries, this.state?.country)))
      .subscribe((countries: ICountry[]) => (this.countriesSharedCollection = countries));
  }
}
