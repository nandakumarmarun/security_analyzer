import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { ITestCheckLisItem } from '../test-check-lis-item.model';
import { sampleWithRequiredData, sampleWithNewData, sampleWithPartialData, sampleWithFullData } from '../test-check-lis-item.test-samples';

import { TestCheckLisItemService } from './test-check-lis-item.service';

const requireRestSample: ITestCheckLisItem = {
  ...sampleWithRequiredData,
};

describe('TestCheckLisItem Service', () => {
  let service: TestCheckLisItemService;
  let httpMock: HttpTestingController;
  let expectedResult: ITestCheckLisItem | ITestCheckLisItem[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(TestCheckLisItemService);
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

    it('should create a TestCheckLisItem', () => {
      const testCheckLisItem = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(testCheckLisItem).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a TestCheckLisItem', () => {
      const testCheckLisItem = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(testCheckLisItem).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a TestCheckLisItem', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of TestCheckLisItem', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a TestCheckLisItem', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addTestCheckLisItemToCollectionIfMissing', () => {
      it('should add a TestCheckLisItem to an empty array', () => {
        const testCheckLisItem: ITestCheckLisItem = sampleWithRequiredData;
        expectedResult = service.addTestCheckLisItemToCollectionIfMissing([], testCheckLisItem);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(testCheckLisItem);
      });

      it('should not add a TestCheckLisItem to an array that contains it', () => {
        const testCheckLisItem: ITestCheckLisItem = sampleWithRequiredData;
        const testCheckLisItemCollection: ITestCheckLisItem[] = [
          {
            ...testCheckLisItem,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addTestCheckLisItemToCollectionIfMissing(testCheckLisItemCollection, testCheckLisItem);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a TestCheckLisItem to an array that doesn't contain it", () => {
        const testCheckLisItem: ITestCheckLisItem = sampleWithRequiredData;
        const testCheckLisItemCollection: ITestCheckLisItem[] = [sampleWithPartialData];
        expectedResult = service.addTestCheckLisItemToCollectionIfMissing(testCheckLisItemCollection, testCheckLisItem);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(testCheckLisItem);
      });

      it('should add only unique TestCheckLisItem to an array', () => {
        const testCheckLisItemArray: ITestCheckLisItem[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const testCheckLisItemCollection: ITestCheckLisItem[] = [sampleWithRequiredData];
        expectedResult = service.addTestCheckLisItemToCollectionIfMissing(testCheckLisItemCollection, ...testCheckLisItemArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const testCheckLisItem: ITestCheckLisItem = sampleWithRequiredData;
        const testCheckLisItem2: ITestCheckLisItem = sampleWithPartialData;
        expectedResult = service.addTestCheckLisItemToCollectionIfMissing([], testCheckLisItem, testCheckLisItem2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(testCheckLisItem);
        expect(expectedResult).toContain(testCheckLisItem2);
      });

      it('should accept null and undefined values', () => {
        const testCheckLisItem: ITestCheckLisItem = sampleWithRequiredData;
        expectedResult = service.addTestCheckLisItemToCollectionIfMissing([], null, testCheckLisItem, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(testCheckLisItem);
      });

      it('should return initial array if no TestCheckLisItem is added', () => {
        const testCheckLisItemCollection: ITestCheckLisItem[] = [sampleWithRequiredData];
        expectedResult = service.addTestCheckLisItemToCollectionIfMissing(testCheckLisItemCollection, undefined, null);
        expect(expectedResult).toEqual(testCheckLisItemCollection);
      });
    });

    describe('compareTestCheckLisItem', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareTestCheckLisItem(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 123 };
        const entity2 = null;

        const compareResult1 = service.compareTestCheckLisItem(entity1, entity2);
        const compareResult2 = service.compareTestCheckLisItem(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 456 };

        const compareResult1 = service.compareTestCheckLisItem(entity1, entity2);
        const compareResult2 = service.compareTestCheckLisItem(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 123 };

        const compareResult1 = service.compareTestCheckLisItem(entity1, entity2);
        const compareResult2 = service.compareTestCheckLisItem(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
