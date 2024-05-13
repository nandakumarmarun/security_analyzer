import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of, EMPTY, Observable } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IState } from '../state.model';
import { StateService } from '../service/state.service';

const stateResolve = (route: ActivatedRouteSnapshot): Observable<null | IState> => {
  const id = route.params['id'];
  if (id) {
    return inject(StateService)
      .find(id)
      .pipe(
        mergeMap((state: HttpResponse<IState>) => {
          if (state.body) {
            return of(state.body);
          } else {
            inject(Router).navigate(['404']);
            return EMPTY;
          }
        }),
      );
  }
  return of(null);
};

export default stateResolve;
