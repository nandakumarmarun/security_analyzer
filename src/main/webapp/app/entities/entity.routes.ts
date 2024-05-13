import { Routes } from '@angular/router';

const routes: Routes = [
  {
    path: 'authority',
    data: { pageTitle: 'myApp.adminAuthority.home.title' },
    loadChildren: () => import('./admin/authority/authority.routes'),
  },
  {
    path: 'user-attributes',
    data: { pageTitle: 'myApp.userAttributes.home.title' },
    loadChildren: () => import('./user-attributes/user-attributes.routes'),
  },
  {
    path: 'application-user',
    data: { pageTitle: 'myApp.applicationUser.home.title' },
    loadChildren: () => import('./application-user/application-user.routes'),
  },
  {
    path: 'country',
    data: { pageTitle: 'myApp.country.home.title' },
    loadChildren: () => import('./country/country.routes'),
  },
  {
    path: 'district',
    data: { pageTitle: 'myApp.district.home.title' },
    loadChildren: () => import('./district/district.routes'),
  },
  {
    path: 'state',
    data: { pageTitle: 'myApp.state.home.title' },
    loadChildren: () => import('./state/state.routes'),
  },
  {
    path: 'city',
    data: { pageTitle: 'myApp.city.home.title' },
    loadChildren: () => import('./city/city.routes'),
  },
  {
    path: 'location',
    data: { pageTitle: 'myApp.location.home.title' },
    loadChildren: () => import('./location/location.routes'),
  },
  {
    path: 'security-test',
    data: { pageTitle: 'myApp.securityTest.home.title' },
    loadChildren: () => import('./security-test/security-test.routes'),
  },
  {
    path: 'test-check-list',
    data: { pageTitle: 'myApp.testCheckList.home.title' },
    loadChildren: () => import('./test-check-list/test-check-list.routes'),
  },
  {
    path: 'test-check-lis-item',
    data: { pageTitle: 'myApp.testCheckLisItem.home.title' },
    loadChildren: () => import('./test-check-lis-item/test-check-lis-item.routes'),
  },
  {
    path: 'check-list',
    data: { pageTitle: 'myApp.checkList.home.title' },
    loadChildren: () => import('./check-list/check-list.routes'),
  },
  {
    path: 'check-lis-item',
    data: { pageTitle: 'myApp.checkLisItem.home.title' },
    loadChildren: () => import('./check-lis-item/check-lis-item.routes'),
  },
  /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
];

export default routes;
