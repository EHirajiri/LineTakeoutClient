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

@Component({
  selector: 'jhi-pay-update',
  templateUrl: './pay-update.component.html',
})
export class PayUpdateComponent implements OnInit {
  isSaving = false;
  customers: ICustomer[] = [];

  editForm = this.fb.group({
    id: [],
    transactionId: [null, [Validators.required]],
    title: [],
    payState: [],
    deliveryState: [],
    paiedDate: [],
    receivedDate: [],
    createdBy: [null, [Validators.maxLength(50)]],
    createdDate: [],
    lastModifiedBy: [null, [Validators.maxLength(50)]],
    lastModifiedDate: [],
    customerId: [],
  });

  constructor(
    protected payService: PayService,
    protected customerService: CustomerService,
    protected activatedRoute: ActivatedRoute,
    private fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ pay }) => {
      if (!pay.id) {
        const today = moment().startOf('day');
        pay.paiedDate = today;
        pay.receivedDate = today;
        pay.createdDate = today;
        pay.lastModifiedDate = today;
      }

      this.updateForm(pay);

      this.customerService.query().subscribe((res: HttpResponse<ICustomer[]>) => (this.customers = res.body || []));
    });
  }

  updateForm(pay: IPay): void {
    this.editForm.patchValue({
      id: pay.id,
      transactionId: pay.transactionId,
      title: pay.title,
      payState: pay.payState,
      deliveryState: pay.deliveryState,
      paiedDate: pay.paiedDate ? pay.paiedDate.format(DATE_TIME_FORMAT) : null,
      receivedDate: pay.receivedDate ? pay.receivedDate.format(DATE_TIME_FORMAT) : null,
      createdBy: pay.createdBy,
      createdDate: pay.createdDate ? pay.createdDate.format(DATE_TIME_FORMAT) : null,
      lastModifiedBy: pay.lastModifiedBy,
      lastModifiedDate: pay.lastModifiedDate ? pay.lastModifiedDate.format(DATE_TIME_FORMAT) : null,
      customerId: pay.customerId,
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
      paiedDate: this.editForm.get(['paiedDate'])!.value ? moment(this.editForm.get(['paiedDate'])!.value, DATE_TIME_FORMAT) : undefined,
      receivedDate: this.editForm.get(['receivedDate'])!.value
        ? moment(this.editForm.get(['receivedDate'])!.value, DATE_TIME_FORMAT)
        : undefined,
      createdBy: this.editForm.get(['createdBy'])!.value,
      createdDate: this.editForm.get(['createdDate'])!.value
        ? moment(this.editForm.get(['createdDate'])!.value, DATE_TIME_FORMAT)
        : undefined,
      lastModifiedBy: this.editForm.get(['lastModifiedBy'])!.value,
      lastModifiedDate: this.editForm.get(['lastModifiedDate'])!.value
        ? moment(this.editForm.get(['lastModifiedDate'])!.value, DATE_TIME_FORMAT)
        : undefined,
      customerId: this.editForm.get(['customerId'])!.value,
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

  trackById(index: number, item: ICustomer): any {
    return item.id;
  }
}
