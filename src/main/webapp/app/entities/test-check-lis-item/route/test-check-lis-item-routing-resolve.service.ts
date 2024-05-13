import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of, EMPTY, Observable } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { ITestCheckLisItem } from '../test-check-lis-item.model';
import { TestCheckLisItemService } from '../service/test-check-lis-item.service';

const testCheckLisItemResolve = (route: ActivatedRouteSnapshot): Observable<null | ITestCheckLisItem> => {
  const id = route.params['id'];
  if (id) {
    return inject(TestCheckLisItemService)
      .find(id)
      .pipe(
        mergeMap((testCheckLisItem: HttpResponse<ITestCheckLisItem>) => {
          if (testCheckLisItem.body) {
            return of(testCheckLisItem.body);
          } else {
            inject(Router).navigate(['404']);
            return EMPTY;
          }
        }),
      );
  }
  return of(null);
};

export default testCheckLisItemResolve;
