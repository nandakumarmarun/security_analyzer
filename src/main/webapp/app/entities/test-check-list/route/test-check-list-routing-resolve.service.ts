import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of, EMPTY, Observable } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { ITestCheckList } from '../test-check-list.model';
import { TestCheckListService } from '../service/test-check-list.service';

const testCheckListResolve = (route: ActivatedRouteSnapshot): Observable<null | ITestCheckList> => {
  const id = route.params['id'];
  if (id) {
    return inject(TestCheckListService)
      .find(id)
      .pipe(
        mergeMap((testCheckList: HttpResponse<ITestCheckList>) => {
          if (testCheckList.body) {
            return of(testCheckList.body);
          } else {
            inject(Router).navigate(['404']);
            return EMPTY;
          }
        }),
      );
  }
  return of(null);
};

export default testCheckListResolve;
