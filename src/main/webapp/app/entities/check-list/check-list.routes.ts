import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import { CheckListComponent } from './list/check-list.component';
import { CheckListDetailComponent } from './detail/check-list-detail.component';
import { CheckListUpdateComponent } from './update/check-list-update.component';
import CheckListResolve from './route/check-list-routing-resolve.service';

const checkListRoute: Routes = [
  {
    path: '',
    component: CheckListComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: CheckListDetailComponent,
    resolve: {
      checkList: CheckListResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: CheckListUpdateComponent,
    resolve: {
      checkList: CheckListResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: CheckListUpdateComponent,
    resolve: {
      checkList: CheckListResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default checkListRoute;
