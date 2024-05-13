import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import { TestCheckLisItemComponent } from './list/test-check-lis-item.component';
import { TestCheckLisItemDetailComponent } from './detail/test-check-lis-item-detail.component';
import { TestCheckLisItemUpdateComponent } from './update/test-check-lis-item-update.component';
import TestCheckLisItemResolve from './route/test-check-lis-item-routing-resolve.service';

const testCheckLisItemRoute: Routes = [
  {
    path: '',
    component: TestCheckLisItemComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: TestCheckLisItemDetailComponent,
    resolve: {
      testCheckLisItem: TestCheckLisItemResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: TestCheckLisItemUpdateComponent,
    resolve: {
      testCheckLisItem: TestCheckLisItemResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: TestCheckLisItemUpdateComponent,
    resolve: {
      testCheckLisItem: TestCheckLisItemResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default testCheckLisItemRoute;
