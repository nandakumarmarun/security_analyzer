import { Component, inject, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { IUser } from 'app/entities/user/user.model';
import { UserService } from 'app/entities/user/service/user.service';
import { IUserAttributes } from 'app/entities/user-attributes/user-attributes.model';
import { UserAttributesService } from 'app/entities/user-attributes/service/user-attributes.service';
import { ApplicationUserService } from '../service/application-user.service';
import { IApplicationUser } from '../application-user.model';
import { ApplicationUserFormService, ApplicationUserFormGroup } from './application-user-form.service';

@Component({
  standalone: true,
  selector: 'jhi-application-user-update',
  templateUrl: './application-user-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class ApplicationUserUpdateComponent implements OnInit {
  isSaving = false;
  applicationUser: IApplicationUser | null = null;

  usersSharedCollection: IUser[] = [];
  userAttributesCollection: IUserAttributes[] = [];

  protected applicationUserService = inject(ApplicationUserService);
  protected applicationUserFormService = inject(ApplicationUserFormService);
  protected userService = inject(UserService);
  protected userAttributesService = inject(UserAttributesService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: ApplicationUserFormGroup = this.applicationUserFormService.createApplicationUserFormGroup();

  compareUser = (o1: IUser | null, o2: IUser | null): boolean => this.userService.compareUser(o1, o2);

  compareUserAttributes = (o1: IUserAttributes | null, o2: IUserAttributes | null): boolean =>
    this.userAttributesService.compareUserAttributes(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ applicationUser }) => {
      this.applicationUser = applicationUser;
      if (applicationUser) {
        this.updateForm(applicationUser);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const applicationUser = this.applicationUserFormService.getApplicationUser(this.editForm);
    if (applicationUser.id !== null) {
      this.subscribeToSaveResponse(this.applicationUserService.update(applicationUser));
    } else {
      this.subscribeToSaveResponse(this.applicationUserService.create(applicationUser));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IApplicationUser>>): void {
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

  protected updateForm(applicationUser: IApplicationUser): void {
    this.applicationUser = applicationUser;
    this.applicationUserFormService.resetForm(this.editForm, applicationUser);

    this.usersSharedCollection = this.userService.addUserToCollectionIfMissing<IUser>(
      this.usersSharedCollection,
      applicationUser.internalUser,
    );
    this.userAttributesCollection = this.userAttributesService.addUserAttributesToCollectionIfMissing<IUserAttributes>(
      this.userAttributesCollection,
      applicationUser.userAttributes,
    );
  }

  protected loadRelationshipsOptions(): void {
    this.userService
      .query()
      .pipe(map((res: HttpResponse<IUser[]>) => res.body ?? []))
      .pipe(map((users: IUser[]) => this.userService.addUserToCollectionIfMissing<IUser>(users, this.applicationUser?.internalUser)))
      .subscribe((users: IUser[]) => (this.usersSharedCollection = users));

    this.userAttributesService
      .query({ filter: 'applicationuser-is-null' })
      .pipe(map((res: HttpResponse<IUserAttributes[]>) => res.body ?? []))
      .pipe(
        map((userAttributes: IUserAttributes[]) =>
          this.userAttributesService.addUserAttributesToCollectionIfMissing<IUserAttributes>(
            userAttributes,
            this.applicationUser?.userAttributes,
          ),
        ),
      )
      .subscribe((userAttributes: IUserAttributes[]) => (this.userAttributesCollection = userAttributes));
  }
}
