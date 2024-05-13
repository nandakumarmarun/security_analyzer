import { Component, inject, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { ICheckList } from 'app/entities/check-list/check-list.model';
import { CheckListService } from 'app/entities/check-list/service/check-list.service';
import { ICheckLisItem } from '../check-lis-item.model';
import { CheckLisItemService } from '../service/check-lis-item.service';
import { CheckLisItemFormService, CheckLisItemFormGroup } from './check-lis-item-form.service';

@Component({
  standalone: true,
  selector: 'jhi-check-lis-item-update',
  templateUrl: './check-lis-item-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class CheckLisItemUpdateComponent implements OnInit {
  isSaving = false;
  checkLisItem: ICheckLisItem | null = null;

  checkListsSharedCollection: ICheckList[] = [];

  protected checkLisItemService = inject(CheckLisItemService);
  protected checkLisItemFormService = inject(CheckLisItemFormService);
  protected checkListService = inject(CheckListService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: CheckLisItemFormGroup = this.checkLisItemFormService.createCheckLisItemFormGroup();

  compareCheckList = (o1: ICheckList | null, o2: ICheckList | null): boolean => this.checkListService.compareCheckList(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ checkLisItem }) => {
      this.checkLisItem = checkLisItem;
      if (checkLisItem) {
        this.updateForm(checkLisItem);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const checkLisItem = this.checkLisItemFormService.getCheckLisItem(this.editForm);
    if (checkLisItem.id !== null) {
      this.subscribeToSaveResponse(this.checkLisItemService.update(checkLisItem));
    } else {
      this.subscribeToSaveResponse(this.checkLisItemService.create(checkLisItem));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ICheckLisItem>>): void {
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

  protected updateForm(checkLisItem: ICheckLisItem): void {
    this.checkLisItem = checkLisItem;
    this.checkLisItemFormService.resetForm(this.editForm, checkLisItem);

    this.checkListsSharedCollection = this.checkListService.addCheckListToCollectionIfMissing<ICheckList>(
      this.checkListsSharedCollection,
      checkLisItem.checkList,
    );
  }

  protected loadRelationshipsOptions(): void {
    this.checkListService
      .query()
      .pipe(map((res: HttpResponse<ICheckList[]>) => res.body ?? []))
      .pipe(
        map((checkLists: ICheckList[]) =>
          this.checkListService.addCheckListToCollectionIfMissing<ICheckList>(checkLists, this.checkLisItem?.checkList),
        ),
      )
      .subscribe((checkLists: ICheckList[]) => (this.checkListsSharedCollection = checkLists));
  }
}
