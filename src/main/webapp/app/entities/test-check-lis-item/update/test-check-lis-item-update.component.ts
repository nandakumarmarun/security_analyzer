import { Component, inject, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { ICheckLisItem } from 'app/entities/check-lis-item/check-lis-item.model';
import { CheckLisItemService } from 'app/entities/check-lis-item/service/check-lis-item.service';
import { ITestCheckList } from 'app/entities/test-check-list/test-check-list.model';
import { TestCheckListService } from 'app/entities/test-check-list/service/test-check-list.service';
import { TestCheckLisItemService } from '../service/test-check-lis-item.service';
import { ITestCheckLisItem } from '../test-check-lis-item.model';
import { TestCheckLisItemFormService, TestCheckLisItemFormGroup } from './test-check-lis-item-form.service';

@Component({
  standalone: true,
  selector: 'jhi-test-check-lis-item-update',
  templateUrl: './test-check-lis-item-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class TestCheckLisItemUpdateComponent implements OnInit {
  isSaving = false;
  testCheckLisItem: ITestCheckLisItem | null = null;

  checklistitemsCollection: ICheckLisItem[] = [];
  testCheckListsSharedCollection: ITestCheckList[] = [];

  protected testCheckLisItemService = inject(TestCheckLisItemService);
  protected testCheckLisItemFormService = inject(TestCheckLisItemFormService);
  protected checkLisItemService = inject(CheckLisItemService);
  protected testCheckListService = inject(TestCheckListService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: TestCheckLisItemFormGroup = this.testCheckLisItemFormService.createTestCheckLisItemFormGroup();

  compareCheckLisItem = (o1: ICheckLisItem | null, o2: ICheckLisItem | null): boolean =>
    this.checkLisItemService.compareCheckLisItem(o1, o2);

  compareTestCheckList = (o1: ITestCheckList | null, o2: ITestCheckList | null): boolean =>
    this.testCheckListService.compareTestCheckList(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ testCheckLisItem }) => {
      this.testCheckLisItem = testCheckLisItem;
      if (testCheckLisItem) {
        this.updateForm(testCheckLisItem);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const testCheckLisItem = this.testCheckLisItemFormService.getTestCheckLisItem(this.editForm);
    if (testCheckLisItem.id !== null) {
      this.subscribeToSaveResponse(this.testCheckLisItemService.update(testCheckLisItem));
    } else {
      this.subscribeToSaveResponse(this.testCheckLisItemService.create(testCheckLisItem));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ITestCheckLisItem>>): void {
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

  protected updateForm(testCheckLisItem: ITestCheckLisItem): void {
    this.testCheckLisItem = testCheckLisItem;
    this.testCheckLisItemFormService.resetForm(this.editForm, testCheckLisItem);

    this.checklistitemsCollection = this.checkLisItemService.addCheckLisItemToCollectionIfMissing<ICheckLisItem>(
      this.checklistitemsCollection,
      testCheckLisItem.checklistitem,
    );
    this.testCheckListsSharedCollection = this.testCheckListService.addTestCheckListToCollectionIfMissing<ITestCheckList>(
      this.testCheckListsSharedCollection,
      testCheckLisItem.testCheckList,
    );
  }

  protected loadRelationshipsOptions(): void {
    this.checkLisItemService
      .query({ filter: 'testchecklisitem-is-null' })
      .pipe(map((res: HttpResponse<ICheckLisItem[]>) => res.body ?? []))
      .pipe(
        map((checkLisItems: ICheckLisItem[]) =>
          this.checkLisItemService.addCheckLisItemToCollectionIfMissing<ICheckLisItem>(checkLisItems, this.testCheckLisItem?.checklistitem),
        ),
      )
      .subscribe((checkLisItems: ICheckLisItem[]) => (this.checklistitemsCollection = checkLisItems));

    this.testCheckListService
      .query()
      .pipe(map((res: HttpResponse<ITestCheckList[]>) => res.body ?? []))
      .pipe(
        map((testCheckLists: ITestCheckList[]) =>
          this.testCheckListService.addTestCheckListToCollectionIfMissing<ITestCheckList>(
            testCheckLists,
            this.testCheckLisItem?.testCheckList,
          ),
        ),
      )
      .subscribe((testCheckLists: ITestCheckList[]) => (this.testCheckListsSharedCollection = testCheckLists));
  }
}
