import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import { SecurityTestComponent } from './list/security-test.component';
import { SecurityTestDetailComponent } from './detail/security-test-detail.component';
import { SecurityTestUpdateComponent } from './update/security-test-update.component';
import SecurityTestResolve from './route/security-test-routing-resolve.service';

const securityTestRoute: Routes = [
  {
    path: '',
    component: SecurityTestComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: SecurityTestDetailComponent,
    resolve: {
      securityTest: SecurityTestResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: SecurityTestUpdateComponent,
    resolve: {
      securityTest: SecurityTestResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: SecurityTestUpdateComponent,
    resolve: {
      securityTest: SecurityTestResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default securityTestRoute;
