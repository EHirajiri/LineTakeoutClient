import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IPay } from 'app/shared/model/pay.model';
import { PayService } from './pay.service';

@Component({
  templateUrl: './pay-delete-dialog.component.html',
})
export class PayDeleteDialogComponent {
  pay?: IPay;

  constructor(protected payService: PayService, public activeModal: NgbActiveModal, protected eventManager: JhiEventManager) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.payService.delete(id).subscribe(() => {
      this.eventManager.broadcast('payListModification');
      this.activeModal.close();
    });
  }
}
