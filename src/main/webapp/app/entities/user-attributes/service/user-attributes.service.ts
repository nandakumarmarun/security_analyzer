import { inject, Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IUserAttributes, NewUserAttributes } from '../user-attributes.model';

export type PartialUpdateUserAttributes = Partial<IUserAttributes> & Pick<IUserAttributes, 'id'>;

export type EntityResponseType = HttpResponse<IUserAttributes>;
export type EntityArrayResponseType = HttpResponse<IUserAttributes[]>;

@Injectable({ providedIn: 'root' })
export class UserAttributesService {
  protected http = inject(HttpClient);
  protected applicationConfigService = inject(ApplicationConfigService);

  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/user-attributes');

  create(userAttributes: NewUserAttributes): Observable<EntityResponseType> {
    return this.http.post<IUserAttributes>(this.resourceUrl, userAttributes, { observe: 'response' });
  }

  update(userAttributes: IUserAttributes): Observable<EntityResponseType> {
    return this.http.put<IUserAttributes>(`${this.resourceUrl}/${this.getUserAttributesIdentifier(userAttributes)}`, userAttributes, {
      observe: 'response',
    });
  }

  partialUpdate(userAttributes: PartialUpdateUserAttributes): Observable<EntityResponseType> {
    return this.http.patch<IUserAttributes>(`${this.resourceUrl}/${this.getUserAttributesIdentifier(userAttributes)}`, userAttributes, {
      observe: 'response',
    });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IUserAttributes>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IUserAttributes[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getUserAttributesIdentifier(userAttributes: Pick<IUserAttributes, 'id'>): number {
    return userAttributes.id;
  }

  compareUserAttributes(o1: Pick<IUserAttributes, 'id'> | null, o2: Pick<IUserAttributes, 'id'> | null): boolean {
    return o1 && o2 ? this.getUserAttributesIdentifier(o1) === this.getUserAttributesIdentifier(o2) : o1 === o2;
  }

  addUserAttributesToCollectionIfMissing<Type extends Pick<IUserAttributes, 'id'>>(
    userAttributesCollection: Type[],
    ...userAttributesToCheck: (Type | null | undefined)[]
  ): Type[] {
    const userAttributes: Type[] = userAttributesToCheck.filter(isPresent);
    if (userAttributes.length > 0) {
      const userAttributesCollectionIdentifiers = userAttributesCollection.map(userAttributesItem =>
        this.getUserAttributesIdentifier(userAttributesItem),
      );
      const userAttributesToAdd = userAttributes.filter(userAttributesItem => {
        const userAttributesIdentifier = this.getUserAttributesIdentifier(userAttributesItem);
        if (userAttributesCollectionIdentifiers.includes(userAttributesIdentifier)) {
          return false;
        }
        userAttributesCollectionIdentifiers.push(userAttributesIdentifier);
        return true;
      });
      return [...userAttributesToAdd, ...userAttributesCollection];
    }
    return userAttributesCollection;
  }
}
