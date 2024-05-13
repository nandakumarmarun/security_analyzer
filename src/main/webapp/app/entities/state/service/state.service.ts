import { inject, Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IState, NewState } from '../state.model';

export type PartialUpdateState = Partial<IState> & Pick<IState, 'id'>;

export type EntityResponseType = HttpResponse<IState>;
export type EntityArrayResponseType = HttpResponse<IState[]>;

@Injectable({ providedIn: 'root' })
export class StateService {
  protected http = inject(HttpClient);
  protected applicationConfigService = inject(ApplicationConfigService);

  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/states');

  create(state: NewState): Observable<EntityResponseType> {
    return this.http.post<IState>(this.resourceUrl, state, { observe: 'response' });
  }

  update(state: IState): Observable<EntityResponseType> {
    return this.http.put<IState>(`${this.resourceUrl}/${this.getStateIdentifier(state)}`, state, { observe: 'response' });
  }

  partialUpdate(state: PartialUpdateState): Observable<EntityResponseType> {
    return this.http.patch<IState>(`${this.resourceUrl}/${this.getStateIdentifier(state)}`, state, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IState>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IState[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getStateIdentifier(state: Pick<IState, 'id'>): number {
    return state.id;
  }

  compareState(o1: Pick<IState, 'id'> | null, o2: Pick<IState, 'id'> | null): boolean {
    return o1 && o2 ? this.getStateIdentifier(o1) === this.getStateIdentifier(o2) : o1 === o2;
  }

  addStateToCollectionIfMissing<Type extends Pick<IState, 'id'>>(
    stateCollection: Type[],
    ...statesToCheck: (Type | null | undefined)[]
  ): Type[] {
    const states: Type[] = statesToCheck.filter(isPresent);
    if (states.length > 0) {
      const stateCollectionIdentifiers = stateCollection.map(stateItem => this.getStateIdentifier(stateItem));
      const statesToAdd = states.filter(stateItem => {
        const stateIdentifier = this.getStateIdentifier(stateItem);
        if (stateCollectionIdentifiers.includes(stateIdentifier)) {
          return false;
        }
        stateCollectionIdentifiers.push(stateIdentifier);
        return true;
      });
      return [...statesToAdd, ...stateCollection];
    }
    return stateCollection;
  }
}
