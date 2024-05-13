import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IUserAttributes } from '../user-attributes.model';
import { sampleWithRequiredData, sampleWithNewData, sampleWithPartialData, sampleWithFullData } from '../user-attributes.test-samples';

import { UserAttributesService } from './user-attributes.service';

const requireRestSample: IUserAttributes = {
  ...sampleWithRequiredData,
};

describe('UserAttributes Service', () => {
  let service: UserAttributesService;
  let httpMock: HttpTestingController;
  let expectedResult: IUserAttributes | IUserAttributes[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(UserAttributesService);
    httpMock = TestBed.inject(HttpTestingController);
  });

  describe('Service methods', () => {
    it('should find an element', () => {
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.find(123).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should create a UserAttributes', () => {
      const userAttributes = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(userAttributes).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a UserAttributes', () => {
      const userAttributes = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(userAttributes).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a UserAttributes', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of UserAttributes', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a UserAttributes', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addUserAttributesToCollectionIfMissing', () => {
      it('should add a UserAttributes to an empty array', () => {
        const userAttributes: IUserAttributes = sampleWithRequiredData;
        expectedResult = service.addUserAttributesToCollectionIfMissing([], userAttributes);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(userAttributes);
      });

      it('should not add a UserAttributes to an array that contains it', () => {
        const userAttributes: IUserAttributes = sampleWithRequiredData;
        const userAttributesCollection: IUserAttributes[] = [
          {
            ...userAttributes,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addUserAttributesToCollectionIfMissing(userAttributesCollection, userAttributes);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a UserAttributes to an array that doesn't contain it", () => {
        const userAttributes: IUserAttributes = sampleWithRequiredData;
        const userAttributesCollection: IUserAttributes[] = [sampleWithPartialData];
        expectedResult = service.addUserAttributesToCollectionIfMissing(userAttributesCollection, userAttributes);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(userAttributes);
      });

      it('should add only unique UserAttributes to an array', () => {
        const userAttributesArray: IUserAttributes[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const userAttributesCollection: IUserAttributes[] = [sampleWithRequiredData];
        expectedResult = service.addUserAttributesToCollectionIfMissing(userAttributesCollection, ...userAttributesArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const userAttributes: IUserAttributes = sampleWithRequiredData;
        const userAttributes2: IUserAttributes = sampleWithPartialData;
        expectedResult = service.addUserAttributesToCollectionIfMissing([], userAttributes, userAttributes2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(userAttributes);
        expect(expectedResult).toContain(userAttributes2);
      });

      it('should accept null and undefined values', () => {
        const userAttributes: IUserAttributes = sampleWithRequiredData;
        expectedResult = service.addUserAttributesToCollectionIfMissing([], null, userAttributes, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(userAttributes);
      });

      it('should return initial array if no UserAttributes is added', () => {
        const userAttributesCollection: IUserAttributes[] = [sampleWithRequiredData];
        expectedResult = service.addUserAttributesToCollectionIfMissing(userAttributesCollection, undefined, null);
        expect(expectedResult).toEqual(userAttributesCollection);
      });
    });

    describe('compareUserAttributes', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareUserAttributes(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 123 };
        const entity2 = null;

        const compareResult1 = service.compareUserAttributes(entity1, entity2);
        const compareResult2 = service.compareUserAttributes(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 456 };

        const compareResult1 = service.compareUserAttributes(entity1, entity2);
        const compareResult2 = service.compareUserAttributes(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 123 };

        const compareResult1 = service.compareUserAttributes(entity1, entity2);
        const compareResult2 = service.compareUserAttributes(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
