import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import { TestCheckListComponent } from './list/test-check-list.component';
import { TestCheckListDetailComponent } from './detail/test-check-list-detail.component';
import { TestCheckListUpdateComponent } from './update/test-check-list-update.component';
import TestCheckListResolve from './route/test-check-list-routing-resolve.service';

const testCheckListRoute: Routes = [
  {
    path: '',
    component: TestCheckListComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: TestCheckListDetailComponent,
    resolve: {
      testCheckList: TestCheckListResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: TestCheckListUpdateComponent,
    resolve: {
      testCheckList: TestCheckListResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: TestCheckListUpdateComponent,
    resolve: {
      testCheckList: TestCheckListResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default testCheckListRoute;
