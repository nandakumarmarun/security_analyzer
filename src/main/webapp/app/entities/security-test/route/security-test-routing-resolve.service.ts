import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of, EMPTY, Observable } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { ISecurityTest } from '../security-test.model';
import { SecurityTestService } from '../service/security-test.service';

const securityTestResolve = (route: ActivatedRouteSnapshot): Observable<null | ISecurityTest> => {
  const id = route.params['id'];
  if (id) {
    return inject(SecurityTestService)
      .find(id)
      .pipe(
        mergeMap((securityTest: HttpResponse<ISecurityTest>) => {
          if (securityTest.body) {
            return of(securityTest.body);
          } else {
            inject(Router).navigate(['404']);
            return EMPTY;
          }
        }),
      );
  }
  return of(null);
};

export default securityTestResolve;
