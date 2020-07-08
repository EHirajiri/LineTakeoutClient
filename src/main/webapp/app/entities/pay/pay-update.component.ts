import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import * as moment from 'moment';
import { DATE_TIME_FORMAT } from 'app/shared/constants/input.constants';

import { IPay, Pay } from 'app/shared/model/pay.model';
import { PayService } from './pay.service';
import { ICustomer } from 'app/shared/model/customer.model';
import { CustomerService } from 'app/entities/customer/customer.service';
import { IOrdered } from 'app/shared/model/ordered.model';
import { OrderedService } from 'app/entities/ordered/ordered.service';

type SelectableEntity = ICustomer | IOrdered;

@Component({
  selector: 'jhi-pay-update',
  templateUrl: './pay-update.component.html',
})
export class PayUpdateComponent implements OnInit {
  isSaving = false;
  customers: ICustomer[] = [];
  ordereds: IOrdered[] = [];

  editForm = this.fb.group({
    id: [],
    transactionId: [null, [Validators.required]],
    title: [],
    payState: [],
    deliveryState: [],
    paidDate: [],
    deliveryDate: [],
    amount: [],
    createdBy: [null, [Validators.maxLength(50)]],
    createdDate: [],
    lastModifiedBy: [null, [Validators.maxLength(50)]],
    lastModifiedDate: [],
    customerId: [],
    orderedId: [],
  });

  constructor(
    protected payService: PayService,
    protected customerService: CustomerService,
    protected orderedService: OrderedService,
    protected activatedRoute: ActivatedRoute,
    private fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ pay }) => {
      if (!pay.id) {
        const today = moment().startOf('day');
        pay.paidDate = today;
        pay.deliveryDate = today;
        pay.createdDate = today;
        pay.lastModifiedDate = today;
      }

      this.updateForm(pay);

      this.customerService.query().subscribe((res: HttpResponse<ICustomer[]>) => (this.customers = res.body || []));

      this.orderedService.query().subscribe((res: HttpResponse<IOrdered[]>) => (this.ordereds = res.body || []));
    });
  }

  updateForm(pay: IPay): void {
    this.editForm.patchValue({
      id: pay.id,
      transactionId: pay.transactionId,
      title: pay.title,
      payState: pay.payState,
      deliveryState: pay.deliveryState,
      paidDate: pay.paidDate ? pay.paidDate.format(DATE_TIME_FORMAT) : null,
      deliveryDate: pay.deliveryDate ? pay.deliveryDate.format(DATE_TIME_FORMAT) : null,
      amount: pay.amount,
      createdBy: pay.createdBy,
      createdDate: pay.createdDate ? pay.createdDate.format(DATE_TIME_FORMAT) : null,
      lastModifiedBy: pay.lastModifiedBy,
      lastModifiedDate: pay.lastModifiedDate ? pay.lastModifiedDate.format(DATE_TIME_FORMAT) : null,
      customerId: pay.customerId,
      orderedId: pay.orderedId,
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const pay = this.createFromForm();
    if (pay.id !== undefined) {
      this.subscribeToSaveResponse(this.payService.update(pay));
    } else {
      this.subscribeToSaveResponse(this.payService.create(pay));
    }
  }

  private createFromForm(): IPay {
    return {
      ...new Pay(),
      id: this.editForm.get(['id'])!.value,
      transactionId: this.editForm.get(['transactionId'])!.value,
      title: this.editForm.get(['title'])!.value,
      payState: this.editForm.get(['payState'])!.value,
      deliveryState: this.editForm.get(['deliveryState'])!.value,
      paidDate: this.editForm.get(['paidDate'])!.value ? moment(this.editForm.get(['paidDate'])!.value, DATE_TIME_FORMAT) : undefined,
      deliveryDate: this.editForm.get(['deliveryDate'])!.value
        ? moment(this.editForm.get(['deliveryDate'])!.value, DATE_TIME_FORMAT)
        : undefined,
      amount: this.editForm.get(['amount'])!.value,
      createdBy: this.editForm.get(['createdBy'])!.value,
      createdDate: this.editForm.get(['createdDate'])!.value
        ? moment(this.editForm.get(['createdDate'])!.value, DATE_TIME_FORMAT)
        : undefined,
      lastModifiedBy: this.editForm.get(['lastModifiedBy'])!.value,
      lastModifiedDate: this.editForm.get(['lastModifiedDate'])!.value
        ? moment(this.editForm.get(['lastModifiedDate'])!.value, DATE_TIME_FORMAT)
        : undefined,
      customerId: this.editForm.get(['customerId'])!.value,
      orderedId: this.editForm.get(['orderedId'])!.value,
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IPay>>): void {
    result.subscribe(
      () => this.onSaveSuccess(),
      () => this.onSaveError()
    );
  }

  protected onSaveSuccess(): void {
    this.isSaving = false;
    this.previousState();
  }

  protected onSaveError(): void {
    this.isSaving = false;
  }

  trackById(index: number, item: SelectableEntity): any {
    return item.id;
  }
}
