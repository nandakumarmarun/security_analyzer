import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of, EMPTY, Observable } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IUserAttributes } from '../user-attributes.model';
import { UserAttributesService } from '../service/user-attributes.service';

const userAttributesResolve = (route: ActivatedRouteSnapshot): Observable<null | IUserAttributes> => {
  const id = route.params['id'];
  if (id) {
    return inject(UserAttributesService)
      .find(id)
      .pipe(
        mergeMap((userAttributes: HttpResponse<IUserAttributes>) => {
          if (userAttributes.body) {
            return of(userAttributes.body);
          } else {
            inject(Router).navigate(['404']);
            return EMPTY;
          }
        }),
      );
  }
  return of(null);
};

export default userAttributesResolve;
