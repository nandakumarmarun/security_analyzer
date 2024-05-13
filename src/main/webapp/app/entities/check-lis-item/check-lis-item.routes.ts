import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import { CheckLisItemComponent } from './list/check-lis-item.component';
import { CheckLisItemDetailComponent } from './detail/check-lis-item-detail.component';
import { CheckLisItemUpdateComponent } from './update/check-lis-item-update.component';
import CheckLisItemResolve from './route/check-lis-item-routing-resolve.service';

const checkLisItemRoute: Routes = [
  {
    path: '',
    component: CheckLisItemComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: CheckLisItemDetailComponent,
    resolve: {
      checkLisItem: CheckLisItemResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: CheckLisItemUpdateComponent,
    resolve: {
      checkLisItem: CheckLisItemResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: CheckLisItemUpdateComponent,
    resolve: {
      checkLisItem: CheckLisItemResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default checkLisItemRoute;
