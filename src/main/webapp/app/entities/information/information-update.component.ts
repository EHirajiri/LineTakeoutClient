import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import * as moment from 'moment';
import { DATE_TIME_FORMAT } from 'app/shared/constants/input.constants';

import { IInformation, Information } from 'app/shared/model/information.model';
import { InformationService } from './information.service';

@Component({
  selector: 'jhi-information-update',
  templateUrl: './information-update.component.html',
})
export class InformationUpdateComponent implements OnInit {
  isSaving = false;

  editForm = this.fb.group({
    id: [],
    key: [null, [Validators.required]],
    value: [],
    createdBy: [null, [Validators.maxLength(50)]],
    createdDate: [],
    lastModifiedBy: [null, [Validators.maxLength(50)]],
    lastModifiedDate: [],
  });

  constructor(protected informationService: InformationService, protected activatedRoute: ActivatedRoute, private fb: FormBuilder) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ information }) => {
      if (!information.id) {
        const today = moment().startOf('day');
        information.createdDate = today;
        information.lastModifiedDate = today;
      }

      this.updateForm(information);
    });
  }

  updateForm(information: IInformation): void {
    this.editForm.patchValue({
      id: information.id,
      key: information.key,
      value: information.value,
      createdBy: information.createdBy,
      createdDate: information.createdDate ? information.createdDate.format(DATE_TIME_FORMAT) : null,
      lastModifiedBy: information.lastModifiedBy,
      lastModifiedDate: information.lastModifiedDate ? information.lastModifiedDate.format(DATE_TIME_FORMAT) : null,
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const information = this.createFromForm();
    if (information.id !== undefined) {
      this.subscribeToSaveResponse(this.informationService.update(information));
    } else {
      this.subscribeToSaveResponse(this.informationService.create(information));
    }
  }

  private createFromForm(): IInformation {
    return {
      ...new Information(),
      id: this.editForm.get(['id'])!.value,
      key: this.editForm.get(['key'])!.value,
      value: this.editForm.get(['value'])!.value,
      createdBy: this.editForm.get(['createdBy'])!.value,
      createdDate: this.editForm.get(['createdDate'])!.value
        ? moment(this.editForm.get(['createdDate'])!.value, DATE_TIME_FORMAT)
        : undefined,
      lastModifiedBy: this.editForm.get(['lastModifiedBy'])!.value,
      lastModifiedDate: this.editForm.get(['lastModifiedDate'])!.value
        ? moment(this.editForm.get(['lastModifiedDate'])!.value, DATE_TIME_FORMAT)
        : undefined,
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IInformation>>): void {
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
}
