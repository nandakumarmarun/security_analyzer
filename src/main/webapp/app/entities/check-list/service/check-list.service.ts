import { inject, Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { ICheckList, NewCheckList } from '../check-list.model';

export type PartialUpdateCheckList = Partial<ICheckList> & Pick<ICheckList, 'id'>;

export type EntityResponseType = HttpResponse<ICheckList>;
export type EntityArrayResponseType = HttpResponse<ICheckList[]>;

@Injectable({ providedIn: 'root' })
export class CheckListService {
  protected http = inject(HttpClient);
  protected applicationConfigService = inject(ApplicationConfigService);

  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/check-lists');

  create(checkList: NewCheckList): Observable<EntityResponseType> {
    return this.http.post<ICheckList>(this.resourceUrl, checkList, { observe: 'response' });
  }

  update(checkList: ICheckList): Observable<EntityResponseType> {
    return this.http.put<ICheckList>(`${this.resourceUrl}/${this.getCheckListIdentifier(checkList)}`, checkList, { observe: 'response' });
  }

  partialUpdate(checkList: PartialUpdateCheckList): Observable<EntityResponseType> {
    return this.http.patch<ICheckList>(`${this.resourceUrl}/${this.getCheckListIdentifier(checkList)}`, checkList, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<ICheckList>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<ICheckList[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getCheckListIdentifier(checkList: Pick<ICheckList, 'id'>): number {
    return checkList.id;
  }

  compareCheckList(o1: Pick<ICheckList, 'id'> | null, o2: Pick<ICheckList, 'id'> | null): boolean {
    return o1 && o2 ? this.getCheckListIdentifier(o1) === this.getCheckListIdentifier(o2) : o1 === o2;
  }

  addCheckListToCollectionIfMissing<Type extends Pick<ICheckList, 'id'>>(
    checkListCollection: Type[],
    ...checkListsToCheck: (Type | null | undefined)[]
  ): Type[] {
    const checkLists: Type[] = checkListsToCheck.filter(isPresent);
    if (checkLists.length > 0) {
      const checkListCollectionIdentifiers = checkListCollection.map(checkListItem => this.getCheckListIdentifier(checkListItem));
      const checkListsToAdd = checkLists.filter(checkListItem => {
        const checkListIdentifier = this.getCheckListIdentifier(checkListItem);
        if (checkListCollectionIdentifiers.includes(checkListIdentifier)) {
          return false;
        }
        checkListCollectionIdentifiers.push(checkListIdentifier);
        return true;
      });
      return [...checkListsToAdd, ...checkListCollection];
    }
    return checkListCollection;
  }
}
