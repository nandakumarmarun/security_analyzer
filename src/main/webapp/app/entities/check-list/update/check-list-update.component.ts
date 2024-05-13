import { Component, inject, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { ICheckList } from '../check-list.model';
import { CheckListService } from '../service/check-list.service';
import { CheckListFormService, CheckListFormGroup } from './check-list-form.service';

@Component({
  standalone: true,
  selector: 'jhi-check-list-update',
  templateUrl: './check-list-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class CheckListUpdateComponent implements OnInit {
  isSaving = false;
  checkList: ICheckList | null = null;

  protected checkListService = inject(CheckListService);
  protected checkListFormService = inject(CheckListFormService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: CheckListFormGroup = this.checkListFormService.createCheckListFormGroup();

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ checkList }) => {
      this.checkList = checkList;
      if (checkList) {
        this.updateForm(checkList);
      }
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const checkList = this.checkListFormService.getCheckList(this.editForm);
    if (checkList.id !== null) {
      this.subscribeToSaveResponse(this.checkListService.update(checkList));
    } else {
      this.subscribeToSaveResponse(this.checkListService.create(checkList));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ICheckList>>): void {
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

  protected updateForm(checkList: ICheckList): void {
    this.checkList = checkList;
    this.checkListFormService.resetForm(this.editForm, checkList);
  }
}
