import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import { StateComponent } from './list/state.component';
import { StateDetailComponent } from './detail/state-detail.component';
import { StateUpdateComponent } from './update/state-update.component';
import StateResolve from './route/state-routing-resolve.service';

const stateRoute: Routes = [
  {
    path: '',
    component: StateComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: StateDetailComponent,
    resolve: {
      state: StateResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: StateUpdateComponent,
    resolve: {
      state: StateResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: StateUpdateComponent,
    resolve: {
      state: StateResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default stateRoute;
