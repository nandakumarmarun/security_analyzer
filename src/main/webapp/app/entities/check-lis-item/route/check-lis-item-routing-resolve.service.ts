import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of, EMPTY, Observable } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { ICheckLisItem } from '../check-lis-item.model';
import { CheckLisItemService } from '../service/check-lis-item.service';

const checkLisItemResolve = (route: ActivatedRouteSnapshot): Observable<null | ICheckLisItem> => {
  const id = route.params['id'];
  if (id) {
    return inject(CheckLisItemService)
      .find(id)
      .pipe(
        mergeMap((checkLisItem: HttpResponse<ICheckLisItem>) => {
          if (checkLisItem.body) {
            return of(checkLisItem.body);
          } else {
            inject(Router).navigate(['404']);
            return EMPTY;
          }
        }),
      );
  }
  return of(null);
};

export default checkLisItemResolve;
