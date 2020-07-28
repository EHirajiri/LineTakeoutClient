import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import * as moment from 'moment';
import { DATE_TIME_FORMAT } from 'app/shared/constants/input.constants';

import { IOrdered, Ordered } from 'app/shared/model/ordered.model';
import { OrderedService } from './ordered.service';
import { ICustomer } from 'app/shared/model/customer.model';
import { CustomerService } from 'app/entities/customer/customer.service';
import { IItem } from 'app/shared/model/item.model';
import { ItemService } from 'app/entities/item/item.service';
import { IOrderItem } from 'app/shared/model/order-item.model';
import { OrderItemService } from 'app/entities/order-item/order-item.service';

type SelectableEntity = ICustomer | IItem | IOrderItem;

@Component({
  selector: 'jhi-ordered-update',
  templateUrl: './ordered-update.component.html',
})
export class OrderedUpdateComponent implements OnInit {
  isSaving = false;
  customers: ICustomer[] = [];
  items: IItem[] = [];
  orderitems: IOrderItem[] = [];

  editForm = this.fb.group({
    id: [],
    orderId: [null, [Validators.required]],
    quantity: [],
    unitPrice: [],
    totalFee: [],
    deliveryState: [null, [Validators.required]],
    deliveryDate: [null, [Validators.required]],
    createdBy: [null, [Validators.maxLength(50)]],
    createdDate: [],
    lastModifiedBy: [null, [Validators.maxLength(50)]],
    lastModifiedDate: [],
    customerId: [],
    itemId: [],
    orderItems: [],
  });

  constructor(
    protected orderedService: OrderedService,
    protected customerService: CustomerService,
    protected itemService: ItemService,
    protected orderItemService: OrderItemService,
    protected activatedRoute: ActivatedRoute,
    private fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ ordered }) => {
      if (!ordered.id) {
        const today = moment().startOf('day');
        ordered.deliveryDate = today;
        ordered.createdDate = today;
        ordered.lastModifiedDate = today;
      }

      this.updateForm(ordered);

      this.customerService.query().subscribe((res: HttpResponse<ICustomer[]>) => (this.customers = res.body || []));

      this.itemService.query().subscribe((res: HttpResponse<IItem[]>) => (this.items = res.body || []));

      this.orderItemService.query().subscribe((res: HttpResponse<IOrderItem[]>) => (this.orderitems = res.body || []));
    });
  }

  updateForm(ordered: IOrdered): void {
    this.editForm.patchValue({
      id: ordered.id,
      orderId: ordered.orderId,
      quantity: ordered.quantity,
      unitPrice: ordered.unitPrice,
      totalFee: ordered.totalFee,
      deliveryState: ordered.deliveryState,
      deliveryDate: ordered.deliveryDate ? ordered.deliveryDate.format(DATE_TIME_FORMAT) : null,
      createdBy: ordered.createdBy,
      createdDate: ordered.createdDate ? ordered.createdDate.format(DATE_TIME_FORMAT) : null,
      lastModifiedBy: ordered.lastModifiedBy,
      lastModifiedDate: ordered.lastModifiedDate ? ordered.lastModifiedDate.format(DATE_TIME_FORMAT) : null,
      customerId: ordered.customerId,
      itemId: ordered.itemId,
      orderItems: ordered.orderItems,
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
      deliveryState: this.editForm.get(['deliveryState'])!.value,
      deliveryDate: this.editForm.get(['deliveryDate'])!.value
        ? moment(this.editForm.get(['deliveryDate'])!.value, DATE_TIME_FORMAT)
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
      itemId: this.editForm.get(['itemId'])!.value,
      orderItems: this.editForm.get(['orderItems'])!.value,
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

  getSelected(selectedVals: IOrderItem[], option: IOrderItem): IOrderItem {
    if (selectedVals) {
      for (let i = 0; i < selectedVals.length; i++) {
        if (option.id === selectedVals[i].id) {
          return selectedVals[i];
        }
      }
    }
    return option;
  }
}
