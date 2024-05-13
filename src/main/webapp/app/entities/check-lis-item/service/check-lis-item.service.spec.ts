import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { ICheckLisItem } from '../check-lis-item.model';
import { sampleWithRequiredData, sampleWithNewData, sampleWithPartialData, sampleWithFullData } from '../check-lis-item.test-samples';

import { CheckLisItemService } from './check-lis-item.service';

const requireRestSample: ICheckLisItem = {
  ...sampleWithRequiredData,
};

describe('CheckLisItem Service', () => {
  let service: CheckLisItemService;
  let httpMock: HttpTestingController;
  let expectedResult: ICheckLisItem | ICheckLisItem[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(CheckLisItemService);
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

    it('should create a CheckLisItem', () => {
      const checkLisItem = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(checkLisItem).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a CheckLisItem', () => {
      const checkLisItem = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(checkLisItem).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a CheckLisItem', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of CheckLisItem', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a CheckLisItem', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addCheckLisItemToCollectionIfMissing', () => {
      it('should add a CheckLisItem to an empty array', () => {
        const checkLisItem: ICheckLisItem = sampleWithRequiredData;
        expectedResult = service.addCheckLisItemToCollectionIfMissing([], checkLisItem);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(checkLisItem);
      });

      it('should not add a CheckLisItem to an array that contains it', () => {
        const checkLisItem: ICheckLisItem = sampleWithRequiredData;
        const checkLisItemCollection: ICheckLisItem[] = [
          {
            ...checkLisItem,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addCheckLisItemToCollectionIfMissing(checkLisItemCollection, checkLisItem);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a CheckLisItem to an array that doesn't contain it", () => {
        const checkLisItem: ICheckLisItem = sampleWithRequiredData;
        const checkLisItemCollection: ICheckLisItem[] = [sampleWithPartialData];
        expectedResult = service.addCheckLisItemToCollectionIfMissing(checkLisItemCollection, checkLisItem);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(checkLisItem);
      });

      it('should add only unique CheckLisItem to an array', () => {
        const checkLisItemArray: ICheckLisItem[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const checkLisItemCollection: ICheckLisItem[] = [sampleWithRequiredData];
        expectedResult = service.addCheckLisItemToCollectionIfMissing(checkLisItemCollection, ...checkLisItemArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const checkLisItem: ICheckLisItem = sampleWithRequiredData;
        const checkLisItem2: ICheckLisItem = sampleWithPartialData;
        expectedResult = service.addCheckLisItemToCollectionIfMissing([], checkLisItem, checkLisItem2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(checkLisItem);
        expect(expectedResult).toContain(checkLisItem2);
      });

      it('should accept null and undefined values', () => {
        const checkLisItem: ICheckLisItem = sampleWithRequiredData;
        expectedResult = service.addCheckLisItemToCollectionIfMissing([], null, checkLisItem, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(checkLisItem);
      });

      it('should return initial array if no CheckLisItem is added', () => {
        const checkLisItemCollection: ICheckLisItem[] = [sampleWithRequiredData];
        expectedResult = service.addCheckLisItemToCollectionIfMissing(checkLisItemCollection, undefined, null);
        expect(expectedResult).toEqual(checkLisItemCollection);
      });
    });

    describe('compareCheckLisItem', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareCheckLisItem(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 123 };
        const entity2 = null;

        const compareResult1 = service.compareCheckLisItem(entity1, entity2);
        const compareResult2 = service.compareCheckLisItem(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 456 };

        const compareResult1 = service.compareCheckLisItem(entity1, entity2);
        const compareResult2 = service.compareCheckLisItem(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 123 };

        const compareResult1 = service.compareCheckLisItem(entity1, entity2);
        const compareResult2 = service.compareCheckLisItem(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
