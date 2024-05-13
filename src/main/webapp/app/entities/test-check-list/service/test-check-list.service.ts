import { inject, Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { ITestCheckList, NewTestCheckList } from '../test-check-list.model';

export type PartialUpdateTestCheckList = Partial<ITestCheckList> & Pick<ITestCheckList, 'id'>;

export type EntityResponseType = HttpResponse<ITestCheckList>;
export type EntityArrayResponseType = HttpResponse<ITestCheckList[]>;

@Injectable({ providedIn: 'root' })
export class TestCheckListService {
  protected http = inject(HttpClient);
  protected applicationConfigService = inject(ApplicationConfigService);

  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/test-check-lists');

  create(testCheckList: NewTestCheckList): Observable<EntityResponseType> {
    return this.http.post<ITestCheckList>(this.resourceUrl, testCheckList, { observe: 'response' });
  }

  update(testCheckList: ITestCheckList): Observable<EntityResponseType> {
    return this.http.put<ITestCheckList>(`${this.resourceUrl}/${this.getTestCheckListIdentifier(testCheckList)}`, testCheckList, {
      observe: 'response',
    });
  }

  partialUpdate(testCheckList: PartialUpdateTestCheckList): Observable<EntityResponseType> {
    return this.http.patch<ITestCheckList>(`${this.resourceUrl}/${this.getTestCheckListIdentifier(testCheckList)}`, testCheckList, {
      observe: 'response',
    });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<ITestCheckList>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<ITestCheckList[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getTestCheckListIdentifier(testCheckList: Pick<ITestCheckList, 'id'>): number {
    return testCheckList.id;
  }

  compareTestCheckList(o1: Pick<ITestCheckList, 'id'> | null, o2: Pick<ITestCheckList, 'id'> | null): boolean {
    return o1 && o2 ? this.getTestCheckListIdentifier(o1) === this.getTestCheckListIdentifier(o2) : o1 === o2;
  }

  addTestCheckListToCollectionIfMissing<Type extends Pick<ITestCheckList, 'id'>>(
    testCheckListCollection: Type[],
    ...testCheckListsToCheck: (Type | null | undefined)[]
  ): Type[] {
    const testCheckLists: Type[] = testCheckListsToCheck.filter(isPresent);
    if (testCheckLists.length > 0) {
      const testCheckListCollectionIdentifiers = testCheckListCollection.map(testCheckListItem =>
        this.getTestCheckListIdentifier(testCheckListItem),
      );
      const testCheckListsToAdd = testCheckLists.filter(testCheckListItem => {
        const testCheckListIdentifier = this.getTestCheckListIdentifier(testCheckListItem);
        if (testCheckListCollectionIdentifiers.includes(testCheckListIdentifier)) {
          return false;
        }
        testCheckListCollectionIdentifiers.push(testCheckListIdentifier);
        return true;
      });
      return [...testCheckListsToAdd, ...testCheckListCollection];
    }
    return testCheckListCollection;
  }
}
