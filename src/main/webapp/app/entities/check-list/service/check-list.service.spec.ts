import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { ICheckList } from '../check-list.model';
import { sampleWithRequiredData, sampleWithNewData, sampleWithPartialData, sampleWithFullData } from '../check-list.test-samples';

import { CheckListService } from './check-list.service';

const requireRestSample: ICheckList = {
  ...sampleWithRequiredData,
};

describe('CheckList Service', () => {
  let service: CheckListService;
  let httpMock: HttpTestingController;
  let expectedResult: ICheckList | ICheckList[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(CheckListService);
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

    it('should create a CheckList', () => {
      const checkList = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(checkList).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a CheckList', () => {
      const checkList = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(checkList).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a CheckList', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of CheckList', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a CheckList', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addCheckListToCollectionIfMissing', () => {
      it('should add a CheckList to an empty array', () => {
        const checkList: ICheckList = sampleWithRequiredData;
        expectedResult = service.addCheckListToCollectionIfMissing([], checkList);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(checkList);
      });

      it('should not add a CheckList to an array that contains it', () => {
        const checkList: ICheckList = sampleWithRequiredData;
        const checkListCollection: ICheckList[] = [
          {
            ...checkList,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addCheckListToCollectionIfMissing(checkListCollection, checkList);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a CheckList to an array that doesn't contain it", () => {
        const checkList: ICheckList = sampleWithRequiredData;
        const checkListCollection: ICheckList[] = [sampleWithPartialData];
        expectedResult = service.addCheckListToCollectionIfMissing(checkListCollection, checkList);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(checkList);
      });

      it('should add only unique CheckList to an array', () => {
        const checkListArray: ICheckList[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const checkListCollection: ICheckList[] = [sampleWithRequiredData];
        expectedResult = service.addCheckListToCollectionIfMissing(checkListCollection, ...checkListArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const checkList: ICheckList = sampleWithRequiredData;
        const checkList2: ICheckList = sampleWithPartialData;
        expectedResult = service.addCheckListToCollectionIfMissing([], checkList, checkList2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(checkList);
        expect(expectedResult).toContain(checkList2);
      });

      it('should accept null and undefined values', () => {
        const checkList: ICheckList = sampleWithRequiredData;
        expectedResult = service.addCheckListToCollectionIfMissing([], null, checkList, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(checkList);
      });

      it('should return initial array if no CheckList is added', () => {
        const checkListCollection: ICheckList[] = [sampleWithRequiredData];
        expectedResult = service.addCheckListToCollectionIfMissing(checkListCollection, undefined, null);
        expect(expectedResult).toEqual(checkListCollection);
      });
    });

    describe('compareCheckList', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareCheckList(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 123 };
        const entity2 = null;

        const compareResult1 = service.compareCheckList(entity1, entity2);
        const compareResult2 = service.compareCheckList(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 456 };

        const compareResult1 = service.compareCheckList(entity1, entity2);
        const compareResult2 = service.compareCheckList(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 123 };

        const compareResult1 = service.compareCheckList(entity1, entity2);
        const compareResult2 = service.compareCheckList(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
