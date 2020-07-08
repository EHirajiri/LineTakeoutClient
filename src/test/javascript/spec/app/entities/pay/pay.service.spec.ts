import { TestBed, getTestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import * as moment from 'moment';
import { DATE_TIME_FORMAT } from 'app/shared/constants/input.constants';
import { PayService } from 'app/entities/pay/pay.service';
import { IPay, Pay } from 'app/shared/model/pay.model';
import { PayState } from 'app/shared/model/enumerations/pay-state.model';
import { DeliveryState } from 'app/shared/model/enumerations/delivery-state.model';

describe('Service Tests', () => {
  describe('Pay Service', () => {
    let injector: TestBed;
    let service: PayService;
    let httpMock: HttpTestingController;
    let elemDefault: IPay;
    let expectedResult: IPay | IPay[] | boolean | null;
    let currentDate: moment.Moment;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
      });
      expectedResult = null;
      injector = getTestBed();
      service = injector.get(PayService);
      httpMock = injector.get(HttpTestingController);
      currentDate = moment();

      elemDefault = new Pay(
        0,
        0,
        'AAAAAAA',
        PayState.ORDERED,
        DeliveryState.PREPARING,
        currentDate,
        currentDate,
        0,
        'AAAAAAA',
        currentDate,
        'AAAAAAA',
        currentDate
      );
    });

    describe('Service methods', () => {
      it('should find an element', () => {
        const returnedFromService = Object.assign(
          {
            paidDate: currentDate.format(DATE_TIME_FORMAT),
            deliveryDate: currentDate.format(DATE_TIME_FORMAT),
            createdDate: currentDate.format(DATE_TIME_FORMAT),
            lastModifiedDate: currentDate.format(DATE_TIME_FORMAT),
          },
          elemDefault
        );

        service.find(123).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'GET' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(elemDefault);
      });

      it('should create a Pay', () => {
        const returnedFromService = Object.assign(
          {
            id: 0,
            paidDate: currentDate.format(DATE_TIME_FORMAT),
            deliveryDate: currentDate.format(DATE_TIME_FORMAT),
            createdDate: currentDate.format(DATE_TIME_FORMAT),
            lastModifiedDate: currentDate.format(DATE_TIME_FORMAT),
          },
          elemDefault
        );

        const expected = Object.assign(
          {
            paidDate: currentDate,
            deliveryDate: currentDate,
            createdDate: currentDate,
            lastModifiedDate: currentDate,
          },
          returnedFromService
        );

        service.create(new Pay()).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should update a Pay', () => {
        const returnedFromService = Object.assign(
          {
            transactionId: 1,
            title: 'BBBBBB',
            payState: 'BBBBBB',
            deliveryState: 'BBBBBB',
            paidDate: currentDate.format(DATE_TIME_FORMAT),
            deliveryDate: currentDate.format(DATE_TIME_FORMAT),
            amount: 1,
            createdBy: 'BBBBBB',
            createdDate: currentDate.format(DATE_TIME_FORMAT),
            lastModifiedBy: 'BBBBBB',
            lastModifiedDate: currentDate.format(DATE_TIME_FORMAT),
          },
          elemDefault
        );

        const expected = Object.assign(
          {
            paidDate: currentDate,
            deliveryDate: currentDate,
            createdDate: currentDate,
            lastModifiedDate: currentDate,
          },
          returnedFromService
        );

        service.update(expected).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PUT' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should return a list of Pay', () => {
        const returnedFromService = Object.assign(
          {
            transactionId: 1,
            title: 'BBBBBB',
            payState: 'BBBBBB',
            deliveryState: 'BBBBBB',
            paidDate: currentDate.format(DATE_TIME_FORMAT),
            deliveryDate: currentDate.format(DATE_TIME_FORMAT),
            amount: 1,
            createdBy: 'BBBBBB',
            createdDate: currentDate.format(DATE_TIME_FORMAT),
            lastModifiedBy: 'BBBBBB',
            lastModifiedDate: currentDate.format(DATE_TIME_FORMAT),
          },
          elemDefault
        );

        const expected = Object.assign(
          {
            paidDate: currentDate,
            deliveryDate: currentDate,
            createdDate: currentDate,
            lastModifiedDate: currentDate,
          },
          returnedFromService
        );

        service.query().subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'GET' });
        req.flush([returnedFromService]);
        httpMock.verify();
        expect(expectedResult).toContainEqual(expected);
      });

      it('should delete a Pay', () => {
        service.delete(123).subscribe(resp => (expectedResult = resp.ok));

        const req = httpMock.expectOne({ method: 'DELETE' });
        req.flush({ status: 200 });
        expect(expectedResult);
      });
    });

    afterEach(() => {
      httpMock.verify();
    });
  });
});
