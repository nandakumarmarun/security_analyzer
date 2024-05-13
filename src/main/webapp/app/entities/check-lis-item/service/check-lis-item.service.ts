import { inject, Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { ICheckLisItem, NewCheckLisItem } from '../check-lis-item.model';

export type PartialUpdateCheckLisItem = Partial<ICheckLisItem> & Pick<ICheckLisItem, 'id'>;

export type EntityResponseType = HttpResponse<ICheckLisItem>;
export type EntityArrayResponseType = HttpResponse<ICheckLisItem[]>;

@Injectable({ providedIn: 'root' })
export class CheckLisItemService {
  protected http = inject(HttpClient);
  protected applicationConfigService = inject(ApplicationConfigService);

  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/check-lis-items');

  create(checkLisItem: NewCheckLisItem): Observable<EntityResponseType> {
    return this.http.post<ICheckLisItem>(this.resourceUrl, checkLisItem, { observe: 'response' });
  }

  update(checkLisItem: ICheckLisItem): Observable<EntityResponseType> {
    return this.http.put<ICheckLisItem>(`${this.resourceUrl}/${this.getCheckLisItemIdentifier(checkLisItem)}`, checkLisItem, {
      observe: 'response',
    });
  }

  partialUpdate(checkLisItem: PartialUpdateCheckLisItem): Observable<EntityResponseType> {
    return this.http.patch<ICheckLisItem>(`${this.resourceUrl}/${this.getCheckLisItemIdentifier(checkLisItem)}`, checkLisItem, {
      observe: 'response',
    });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<ICheckLisItem>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<ICheckLisItem[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getCheckLisItemIdentifier(checkLisItem: Pick<ICheckLisItem, 'id'>): number {
    return checkLisItem.id;
  }

  compareCheckLisItem(o1: Pick<ICheckLisItem, 'id'> | null, o2: Pick<ICheckLisItem, 'id'> | null): boolean {
    return o1 && o2 ? this.getCheckLisItemIdentifier(o1) === this.getCheckLisItemIdentifier(o2) : o1 === o2;
  }

  addCheckLisItemToCollectionIfMissing<Type extends Pick<ICheckLisItem, 'id'>>(
    checkLisItemCollection: Type[],
    ...checkLisItemsToCheck: (Type | null | undefined)[]
  ): Type[] {
    const checkLisItems: Type[] = checkLisItemsToCheck.filter(isPresent);
    if (checkLisItems.length > 0) {
      const checkLisItemCollectionIdentifiers = checkLisItemCollection.map(checkLisItemItem =>
        this.getCheckLisItemIdentifier(checkLisItemItem),
      );
      const checkLisItemsToAdd = checkLisItems.filter(checkLisItemItem => {
        const checkLisItemIdentifier = this.getCheckLisItemIdentifier(checkLisItemItem);
        if (checkLisItemCollectionIdentifiers.includes(checkLisItemIdentifier)) {
          return false;
        }
        checkLisItemCollectionIdentifiers.push(checkLisItemIdentifier);
        return true;
      });
      return [...checkLisItemsToAdd, ...checkLisItemCollection];
    }
    return checkLisItemCollection;
  }
}
