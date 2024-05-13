import { inject, Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { ITestCheckLisItem, NewTestCheckLisItem } from '../test-check-lis-item.model';

export type PartialUpdateTestCheckLisItem = Partial<ITestCheckLisItem> & Pick<ITestCheckLisItem, 'id'>;

export type EntityResponseType = HttpResponse<ITestCheckLisItem>;
export type EntityArrayResponseType = HttpResponse<ITestCheckLisItem[]>;

@Injectable({ providedIn: 'root' })
export class TestCheckLisItemService {
  protected http = inject(HttpClient);
  protected applicationConfigService = inject(ApplicationConfigService);

  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/test-check-lis-items');

  create(testCheckLisItem: NewTestCheckLisItem): Observable<EntityResponseType> {
    return this.http.post<ITestCheckLisItem>(this.resourceUrl, testCheckLisItem, { observe: 'response' });
  }

  update(testCheckLisItem: ITestCheckLisItem): Observable<EntityResponseType> {
    return this.http.put<ITestCheckLisItem>(
      `${this.resourceUrl}/${this.getTestCheckLisItemIdentifier(testCheckLisItem)}`,
      testCheckLisItem,
      { observe: 'response' },
    );
  }

  partialUpdate(testCheckLisItem: PartialUpdateTestCheckLisItem): Observable<EntityResponseType> {
    return this.http.patch<ITestCheckLisItem>(
      `${this.resourceUrl}/${this.getTestCheckLisItemIdentifier(testCheckLisItem)}`,
      testCheckLisItem,
      { observe: 'response' },
    );
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<ITestCheckLisItem>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<ITestCheckLisItem[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getTestCheckLisItemIdentifier(testCheckLisItem: Pick<ITestCheckLisItem, 'id'>): number {
    return testCheckLisItem.id;
  }

  compareTestCheckLisItem(o1: Pick<ITestCheckLisItem, 'id'> | null, o2: Pick<ITestCheckLisItem, 'id'> | null): boolean {
    return o1 && o2 ? this.getTestCheckLisItemIdentifier(o1) === this.getTestCheckLisItemIdentifier(o2) : o1 === o2;
  }

  addTestCheckLisItemToCollectionIfMissing<Type extends Pick<ITestCheckLisItem, 'id'>>(
    testCheckLisItemCollection: Type[],
    ...testCheckLisItemsToCheck: (Type | null | undefined)[]
  ): Type[] {
    const testCheckLisItems: Type[] = testCheckLisItemsToCheck.filter(isPresent);
    if (testCheckLisItems.length > 0) {
      const testCheckLisItemCollectionIdentifiers = testCheckLisItemCollection.map(testCheckLisItemItem =>
        this.getTestCheckLisItemIdentifier(testCheckLisItemItem),
      );
      const testCheckLisItemsToAdd = testCheckLisItems.filter(testCheckLisItemItem => {
        const testCheckLisItemIdentifier = this.getTestCheckLisItemIdentifier(testCheckLisItemItem);
        if (testCheckLisItemCollectionIdentifiers.includes(testCheckLisItemIdentifier)) {
          return false;
        }
        testCheckLisItemCollectionIdentifiers.push(testCheckLisItemIdentifier);
        return true;
      });
      return [...testCheckLisItemsToAdd, ...testCheckLisItemCollection];
    }
    return testCheckLisItemCollection;
  }
}
