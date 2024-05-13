import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import { UserAttributesComponent } from './list/user-attributes.component';
import { UserAttributesDetailComponent } from './detail/user-attributes-detail.component';
import { UserAttributesUpdateComponent } from './update/user-attributes-update.component';
import UserAttributesResolve from './route/user-attributes-routing-resolve.service';

const userAttributesRoute: Routes = [
  {
    path: '',
    component: UserAttributesComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: UserAttributesDetailComponent,
    resolve: {
      userAttributes: UserAttributesResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: UserAttributesUpdateComponent,
    resolve: {
      userAttributes: UserAttributesResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: UserAttributesUpdateComponent,
    resolve: {
      userAttributes: UserAttributesResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default userAttributesRoute;
