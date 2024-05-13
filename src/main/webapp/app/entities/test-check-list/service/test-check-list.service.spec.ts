import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { ITestCheckList } from '../test-check-list.model';
import { sampleWithRequiredData, sampleWithNewData, sampleWithPartialData, sampleWithFullData } from '../test-check-list.test-samples';

import { TestCheckListService } from './test-check-list.service';

const requireRestSample: ITestCheckList = {
  ...sampleWithRequiredData,
};

describe('TestCheckList Service', () => {
  let service: TestCheckListService;
  let httpMock: HttpTestingController;
  let expectedResult: ITestCheckList | ITestCheckList[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(TestCheckListService);
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

    it('should create a TestCheckList', () => {
      const testCheckList = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(testCheckList).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a TestCheckList', () => {
      const testCheckList = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(testCheckList).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a TestCheckList', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of TestCheckList', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a TestCheckList', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addTestCheckListToCollectionIfMissing', () => {
      it('should add a TestCheckList to an empty array', () => {
        const testCheckList: ITestCheckList = sampleWithRequiredData;
        expectedResult = service.addTestCheckListToCollectionIfMissing([], testCheckList);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(testCheckList);
      });

      it('should not add a TestCheckList to an array that contains it', () => {
        const testCheckList: ITestCheckList = sampleWithRequiredData;
        const testCheckListCollection: ITestCheckList[] = [
          {
            ...testCheckList,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addTestCheckListToCollectionIfMissing(testCheckListCollection, testCheckList);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a TestCheckList to an array that doesn't contain it", () => {
        const testCheckList: ITestCheckList = sampleWithRequiredData;
        const testCheckListCollection: ITestCheckList[] = [sampleWithPartialData];
        expectedResult = service.addTestCheckListToCollectionIfMissing(testCheckListCollection, testCheckList);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(testCheckList);
      });

      it('should add only unique TestCheckList to an array', () => {
        const testCheckListArray: ITestCheckList[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const testCheckListCollection: ITestCheckList[] = [sampleWithRequiredData];
        expectedResult = service.addTestCheckListToCollectionIfMissing(testCheckListCollection, ...testCheckListArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const testCheckList: ITestCheckList = sampleWithRequiredData;
        const testCheckList2: ITestCheckList = sampleWithPartialData;
        expectedResult = service.addTestCheckListToCollectionIfMissing([], testCheckList, testCheckList2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(testCheckList);
        expect(expectedResult).toContain(testCheckList2);
      });

      it('should accept null and undefined values', () => {
        const testCheckList: ITestCheckList = sampleWithRequiredData;
        expectedResult = service.addTestCheckListToCollectionIfMissing([], null, testCheckList, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(testCheckList);
      });

      it('should return initial array if no TestCheckList is added', () => {
        const testCheckListCollection: ITestCheckList[] = [sampleWithRequiredData];
        expectedResult = service.addTestCheckListToCollectionIfMissing(testCheckListCollection, undefined, null);
        expect(expectedResult).toEqual(testCheckListCollection);
      });
    });

    describe('compareTestCheckList', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareTestCheckList(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 123 };
        const entity2 = null;

        const compareResult1 = service.compareTestCheckList(entity1, entity2);
        const compareResult2 = service.compareTestCheckList(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 456 };

        const compareResult1 = service.compareTestCheckList(entity1, entity2);
        const compareResult2 = service.compareTestCheckList(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 123 };

        const compareResult1 = service.compareTestCheckList(entity1, entity2);
        const compareResult2 = service.compareTestCheckList(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
