import { Component, inject, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { ICheckList } from 'app/entities/check-list/check-list.model';
import { CheckListService } from 'app/entities/check-list/service/check-list.service';
import { ISecurityTest } from 'app/entities/security-test/security-test.model';
import { SecurityTestService } from 'app/entities/security-test/service/security-test.service';
import { TestCheckListService } from '../service/test-check-list.service';
import { ITestCheckList } from '../test-check-list.model';
import { TestCheckListFormService, TestCheckListFormGroup } from './test-check-list-form.service';

@Component({
  standalone: true,
  selector: 'jhi-test-check-list-update',
  templateUrl: './test-check-list-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class TestCheckListUpdateComponent implements OnInit {
  isSaving = false;
  testCheckList: ITestCheckList | null = null;

  checkListsCollection: ICheckList[] = [];
  securityTestsSharedCollection: ISecurityTest[] = [];

  protected testCheckListService = inject(TestCheckListService);
  protected testCheckListFormService = inject(TestCheckListFormService);
  protected checkListService = inject(CheckListService);
  protected securityTestService = inject(SecurityTestService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: TestCheckListFormGroup = this.testCheckListFormService.createTestCheckListFormGroup();

  compareCheckList = (o1: ICheckList | null, o2: ICheckList | null): boolean => this.checkListService.compareCheckList(o1, o2);

  compareSecurityTest = (o1: ISecurityTest | null, o2: ISecurityTest | null): boolean =>
    this.securityTestService.compareSecurityTest(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ testCheckList }) => {
      this.testCheckList = testCheckList;
      if (testCheckList) {
        this.updateForm(testCheckList);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const testCheckList = this.testCheckListFormService.getTestCheckList(this.editForm);
    if (testCheckList.id !== null) {
      this.subscribeToSaveResponse(this.testCheckListService.update(testCheckList));
    } else {
      this.subscribeToSaveResponse(this.testCheckListService.create(testCheckList));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ITestCheckList>>): void {
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

  protected updateForm(testCheckList: ITestCheckList): void {
    this.testCheckList = testCheckList;
    this.testCheckListFormService.resetForm(this.editForm, testCheckList);

    this.checkListsCollection = this.checkListService.addCheckListToCollectionIfMissing<ICheckList>(
      this.checkListsCollection,
      testCheckList.checkList,
    );
    this.securityTestsSharedCollection = this.securityTestService.addSecurityTestToCollectionIfMissing<ISecurityTest>(
      this.securityTestsSharedCollection,
      testCheckList.securityTest,
    );
  }

  protected loadRelationshipsOptions(): void {
    this.checkListService
      .query({ filter: 'testchecklist-is-null' })
      .pipe(map((res: HttpResponse<ICheckList[]>) => res.body ?? []))
      .pipe(
        map((checkLists: ICheckList[]) =>
          this.checkListService.addCheckListToCollectionIfMissing<ICheckList>(checkLists, this.testCheckList?.checkList),
        ),
      )
      .subscribe((checkLists: ICheckList[]) => (this.checkListsCollection = checkLists));

    this.securityTestService
      .query()
      .pipe(map((res: HttpResponse<ISecurityTest[]>) => res.body ?? []))
      .pipe(
        map((securityTests: ISecurityTest[]) =>
          this.securityTestService.addSecurityTestToCollectionIfMissing<ISecurityTest>(securityTests, this.testCheckList?.securityTest),
        ),
      )
      .subscribe((securityTests: ISecurityTest[]) => (this.securityTestsSharedCollection = securityTests));
  }
}
