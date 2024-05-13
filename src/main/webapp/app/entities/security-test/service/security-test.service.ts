import { inject, Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { ISecurityTest, NewSecurityTest } from '../security-test.model';

export type PartialUpdateSecurityTest = Partial<ISecurityTest> & Pick<ISecurityTest, 'id'>;

export type EntityResponseType = HttpResponse<ISecurityTest>;
export type EntityArrayResponseType = HttpResponse<ISecurityTest[]>;

@Injectable({ providedIn: 'root' })
export class SecurityTestService {
  protected http = inject(HttpClient);
  protected applicationConfigService = inject(ApplicationConfigService);

  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/security-tests');

  create(securityTest: NewSecurityTest): Observable<EntityResponseType> {
    return this.http.post<ISecurityTest>(this.resourceUrl, securityTest, { observe: 'response' });
  }

  update(securityTest: ISecurityTest): Observable<EntityResponseType> {
    return this.http.put<ISecurityTest>(`${this.resourceUrl}/${this.getSecurityTestIdentifier(securityTest)}`, securityTest, {
      observe: 'response',
    });
  }

  partialUpdate(securityTest: PartialUpdateSecurityTest): Observable<EntityResponseType> {
    return this.http.patch<ISecurityTest>(`${this.resourceUrl}/${this.getSecurityTestIdentifier(securityTest)}`, securityTest, {
      observe: 'response',
    });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<ISecurityTest>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<ISecurityTest[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getSecurityTestIdentifier(securityTest: Pick<ISecurityTest, 'id'>): number {
    return securityTest.id;
  }

  compareSecurityTest(o1: Pick<ISecurityTest, 'id'> | null, o2: Pick<ISecurityTest, 'id'> | null): boolean {
    return o1 && o2 ? this.getSecurityTestIdentifier(o1) === this.getSecurityTestIdentifier(o2) : o1 === o2;
  }

  addSecurityTestToCollectionIfMissing<Type extends Pick<ISecurityTest, 'id'>>(
    securityTestCollection: Type[],
    ...securityTestsToCheck: (Type | null | undefined)[]
  ): Type[] {
    const securityTests: Type[] = securityTestsToCheck.filter(isPresent);
    if (securityTests.length > 0) {
      const securityTestCollectionIdentifiers = securityTestCollection.map(securityTestItem =>
        this.getSecurityTestIdentifier(securityTestItem),
      );
      const securityTestsToAdd = securityTests.filter(securityTestItem => {
        const securityTestIdentifier = this.getSecurityTestIdentifier(securityTestItem);
        if (securityTestCollectionIdentifiers.includes(securityTestIdentifier)) {
          return false;
        }
        securityTestCollectionIdentifiers.push(securityTestIdentifier);
        return true;
      });
      return [...securityTestsToAdd, ...securityTestCollection];
    }
    return securityTestCollection;
  }
}
