import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import * as moment from 'moment';
import { DATE_TIME_FORMAT } from 'app/shared/constants/input.constants';

import { IOrdered, Ordered } from 'app/shared/model/ordered.model';
import { OrderedService } from './ordered.service';
import { IPay } from 'app/shared/model/pay.model';
import { PayService } from 'app/entities/pay/pay.service';
import { ICustomer } from 'app/shared/model/customer.model';
import { CustomerService } from 'app/entities/customer/customer.service';
import { IItem } from 'app/shared/model/item.model';
import { ItemService } from 'app/entities/item/item.service';

type SelectableEntity = IPay | ICustomer | IItem;

@Component({
  selector: 'jhi-ordered-update',
  templateUrl: './ordered-update.component.html',
})
export class OrderedUpdateComponent implements OnInit {
  isSaving = false;
  pays: IPay[] = [];
  customers: ICustomer[] = [];
  items: IItem[] = [];

  editForm = this.fb.group({
    id: [],
    orderId: [null, [Validators.required]],
    quantity: [],
    unitPrice: [],
    totalFee: [],
    createdBy: [null, [Validators.maxLength(50)]],
    createdDate: [],
    lastModifiedBy: [null, [Validators.maxLength(50)]],
    lastModifiedDate: [],
    payId: [],
    customerId: [],
    itemId: [],
  });

  constructor(
    protected orderedService: OrderedService,
    protected payService: PayService,
    protected customerService: CustomerService,
    protected itemService: ItemService,
    protected activatedRoute: ActivatedRoute,
    private fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ ordered }) => {
      if (!ordered.id) {
        const today = moment().startOf('day');
        ordered.createdDate = today;
        ordered.lastModifiedDate = today;
      }

      this.updateForm(ordered);

      this.payService
        .query({ filter: 'ordered-is-null' })
        .pipe(
          map((res: HttpResponse<IPay[]>) => {
            return res.body || [];
          })
        )
        .subscribe((resBody: IPay[]) => {
          if (!ordered.payId) {
            this.pays = resBody;
          } else {
            this.payService
              .find(ordered.payId)
              .pipe(
                map((subRes: HttpResponse<IPay>) => {
                  return subRes.body ? [subRes.body].concat(resBody) : resBody;
                })
              )
              .subscribe((concatRes: IPay[]) => (this.pays = concatRes));
          }
        });

      this.customerService.query().subscribe((res: HttpResponse<ICustomer[]>) => (this.customers = res.body || []));

      this.itemService.query().subscribe((res: HttpResponse<IItem[]>) => (this.items = res.body || []));
    });
  }

  updateForm(ordered: IOrdered): void {
    this.editForm.patchValue({
      id: ordered.id,
      orderId: ordered.orderId,
      quantity: ordered.quantity,
      unitPrice: ordered.unitPrice,
      totalFee: ordered.totalFee,
      createdBy: ordered.createdBy,
      createdDate: ordered.createdDate ? ordered.createdDate.format(DATE_TIME_FORMAT) : null,
      lastModifiedBy: ordered.lastModifiedBy,
      lastModifiedDate: ordered.lastModifiedDate ? ordered.lastModifiedDate.format(DATE_TIME_FORMAT) : null,
      payId: ordered.payId,
      customerId: ordered.customerId,
      itemId: ordered.itemId,
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const ordered = this.createFromForm();
    if (ordered.id !== undefined) {
      this.subscribeToSaveResponse(this.orderedService.update(ordered));
    } else {
      this.subscribeToSaveResponse(this.orderedService.create(ordered));
    }
  }

  private createFromForm(): IOrdered {
    return {
      ...new Ordered(),
      id: this.editForm.get(['id'])!.value,
      orderId: this.editForm.get(['orderId'])!.value,
      quantity: this.editForm.get(['quantity'])!.value,
      unitPrice: this.editForm.get(['unitPrice'])!.value,
      totalFee: this.editForm.get(['totalFee'])!.value,
      createdBy: this.editForm.get(['createdBy'])!.value,
      createdDate: this.editForm.get(['createdDate'])!.value
        ? moment(this.editForm.get(['createdDate'])!.value, DATE_TIME_FORMAT)
        : undefined,
      lastModifiedBy: this.editForm.get(['lastModifiedBy'])!.value,
      lastModifiedDate: this.editForm.get(['lastModifiedDate'])!.value
        ? moment(this.editForm.get(['lastModifiedDate'])!.value, DATE_TIME_FORMAT)
        : undefined,
      payId: this.editForm.get(['payId'])!.value,
      customerId: this.editForm.get(['customerId'])!.value,
      itemId: this.editForm.get(['itemId'])!.value,
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IOrdered>>): void {
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
