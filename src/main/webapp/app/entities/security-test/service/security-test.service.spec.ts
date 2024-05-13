import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { ISecurityTest } from '../security-test.model';
import { sampleWithRequiredData, sampleWithNewData, sampleWithPartialData, sampleWithFullData } from '../security-test.test-samples';

import { SecurityTestService } from './security-test.service';

const requireRestSample: ISecurityTest = {
  ...sampleWithRequiredData,
};

describe('SecurityTest Service', () => {
  let service: SecurityTestService;
  let httpMock: HttpTestingController;
  let expectedResult: ISecurityTest | ISecurityTest[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(SecurityTestService);
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

    it('should create a SecurityTest', () => {
      const securityTest = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(securityTest).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a SecurityTest', () => {
      const securityTest = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(securityTest).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a SecurityTest', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of SecurityTest', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a SecurityTest', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addSecurityTestToCollectionIfMissing', () => {
      it('should add a SecurityTest to an empty array', () => {
        const securityTest: ISecurityTest = sampleWithRequiredData;
        expectedResult = service.addSecurityTestToCollectionIfMissing([], securityTest);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(securityTest);
      });

      it('should not add a SecurityTest to an array that contains it', () => {
        const securityTest: ISecurityTest = sampleWithRequiredData;
        const securityTestCollection: ISecurityTest[] = [
          {
            ...securityTest,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addSecurityTestToCollectionIfMissing(securityTestCollection, securityTest);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a SecurityTest to an array that doesn't contain it", () => {
        const securityTest: ISecurityTest = sampleWithRequiredData;
        const securityTestCollection: ISecurityTest[] = [sampleWithPartialData];
        expectedResult = service.addSecurityTestToCollectionIfMissing(securityTestCollection, securityTest);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(securityTest);
      });

      it('should add only unique SecurityTest to an array', () => {
        const securityTestArray: ISecurityTest[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const securityTestCollection: ISecurityTest[] = [sampleWithRequiredData];
        expectedResult = service.addSecurityTestToCollectionIfMissing(securityTestCollection, ...securityTestArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const securityTest: ISecurityTest = sampleWithRequiredData;
        const securityTest2: ISecurityTest = sampleWithPartialData;
        expectedResult = service.addSecurityTestToCollectionIfMissing([], securityTest, securityTest2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(securityTest);
        expect(expectedResult).toContain(securityTest2);
      });

      it('should accept null and undefined values', () => {
        const securityTest: ISecurityTest = sampleWithRequiredData;
        expectedResult = service.addSecurityTestToCollectionIfMissing([], null, securityTest, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(securityTest);
      });

      it('should return initial array if no SecurityTest is added', () => {
        const securityTestCollection: ISecurityTest[] = [sampleWithRequiredData];
        expectedResult = service.addSecurityTestToCollectionIfMissing(securityTestCollection, undefined, null);
        expect(expectedResult).toEqual(securityTestCollection);
      });
    });

    describe('compareSecurityTest', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareSecurityTest(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 123 };
        const entity2 = null;

        const compareResult1 = service.compareSecurityTest(entity1, entity2);
        const compareResult2 = service.compareSecurityTest(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 456 };

        const compareResult1 = service.compareSecurityTest(entity1, entity2);
        const compareResult2 = service.compareSecurityTest(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 123 };

        const compareResult1 = service.compareSecurityTest(entity1, entity2);
        const compareResult2 = service.compareSecurityTest(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
