import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of, EMPTY, Observable } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { ICheckList } from '../check-list.model';
import { CheckListService } from '../service/check-list.service';

const checkListResolve = (route: ActivatedRouteSnapshot): Observable<null | ICheckList> => {
  const id = route.params['id'];
  if (id) {
    return inject(CheckListService)
      .find(id)
      .pipe(
        mergeMap((checkList: HttpResponse<ICheckList>) => {
          if (checkList.body) {
            return of(checkList.body);
          } else {
            inject(Router).navigate(['404']);
            return EMPTY;
          }
        }),
      );
  }
  return of(null);
};

export default checkListResolve;
