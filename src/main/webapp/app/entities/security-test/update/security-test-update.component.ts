import { Component, inject, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { IApplicationUser } from 'app/entities/application-user/application-user.model';
import { ApplicationUserService } from 'app/entities/application-user/service/application-user.service';
import { SecurityLevel } from 'app/entities/enumerations/security-level.model';
import { SecurityTestService } from '../service/security-test.service';
import { ISecurityTest } from '../security-test.model';
import { SecurityTestFormService, SecurityTestFormGroup } from './security-test-form.service';

@Component({
  standalone: true,
  selector: 'jhi-security-test-update',
  templateUrl: './security-test-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class SecurityTestUpdateComponent implements OnInit {
  isSaving = false;
  securityTest: ISecurityTest | null = null;
  securityLevelValues = Object.keys(SecurityLevel);

  applicationUsersSharedCollection: IApplicationUser[] = [];

  protected securityTestService = inject(SecurityTestService);
  protected securityTestFormService = inject(SecurityTestFormService);
  protected applicationUserService = inject(ApplicationUserService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: SecurityTestFormGroup = this.securityTestFormService.createSecurityTestFormGroup();

  compareApplicationUser = (o1: IApplicationUser | null, o2: IApplicationUser | null): boolean =>
    this.applicationUserService.compareApplicationUser(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ securityTest }) => {
      this.securityTest = securityTest;
      if (securityTest) {
        this.updateForm(securityTest);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const securityTest = this.securityTestFormService.getSecurityTest(this.editForm);
    if (securityTest.id !== null) {
      this.subscribeToSaveResponse(this.securityTestService.update(securityTest));
    } else {
      this.subscribeToSaveResponse(this.securityTestService.create(securityTest));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ISecurityTest>>): void {
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

  protected updateForm(securityTest: ISecurityTest): void {
    this.securityTest = securityTest;
    this.securityTestFormService.resetForm(this.editForm, securityTest);

    this.applicationUsersSharedCollection = this.applicationUserService.addApplicationUserToCollectionIfMissing<IApplicationUser>(
      this.applicationUsersSharedCollection,
      securityTest.applicationUser,
    );
  }

  protected loadRelationshipsOptions(): void {
    this.applicationUserService
      .query()
      .pipe(map((res: HttpResponse<IApplicationUser[]>) => res.body ?? []))
      .pipe(
        map((applicationUsers: IApplicationUser[]) =>
          this.applicationUserService.addApplicationUserToCollectionIfMissing<IApplicationUser>(
            applicationUsers,
            this.securityTest?.applicationUser,
          ),
        ),
      )
      .subscribe((applicationUsers: IApplicationUser[]) => (this.applicationUsersSharedCollection = applicationUsers));
  }
}
