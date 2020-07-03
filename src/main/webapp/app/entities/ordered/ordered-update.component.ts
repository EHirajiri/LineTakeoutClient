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

type SelectableEntity = ICustomer | IItem;

@Component({
  selector: 'jhi-ordered-update',
  templateUrl: './ordered-update.component.html',
})
export class OrderedUpdateComponent implements OnInit {
  isSaving = false;
  customers: ICustomer[] = [];
  items: IItem[] = [];

  editForm = this.fb.group({
    id: [],
    quantity: [null, [Validators.required]],
    totalFee: [],
    createdBy: [null, [Validators.required, Validators.maxLength(50)]],
    createdDate: [],
    lastModifiedBy: [null, [Validators.maxLength(50)]],
    lastModifiedDate: [],
    customerId: [null, Validators.required],
    itemId: [null, Validators.required],
  });

  constructor(
    protected orderedService: OrderedService,
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

      this.customerService.query().subscribe((res: HttpResponse<ICustomer[]>) => (this.customers = res.body || []));

      this.itemService.query().subscribe((res: HttpResponse<IItem[]>) => (this.items = res.body || []));
    });
  }

  updateForm(ordered: IOrdered): void {
    this.editForm.patchValue({
      id: ordered.id,
      quantity: ordered.quantity,
      totalFee: ordered.totalFee,
      createdBy: ordered.createdBy,
      createdDate: ordered.createdDate ? ordered.createdDate.format(DATE_TIME_FORMAT) : null,
      lastModifiedBy: ordered.lastModifiedBy,
      lastModifiedDate: ordered.lastModifiedDate ? ordered.lastModifiedDate.format(DATE_TIME_FORMAT) : null,
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
      quantity: this.editForm.get(['quantity'])!.value,
      totalFee: this.editForm.get(['totalFee'])!.value,
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
