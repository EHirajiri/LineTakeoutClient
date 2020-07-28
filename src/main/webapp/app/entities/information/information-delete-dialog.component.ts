import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IInformation } from 'app/shared/model/information.model';
import { InformationService } from './information.service';

@Component({
  templateUrl: './information-delete-dialog.component.html',
})
export class InformationDeleteDialogComponent {
  information?: IInformation;

  constructor(
    protected informationService: InformationService,
    public activeModal: NgbActiveModal,
    protected eventManager: JhiEventManager
  ) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.informationService.delete(id).subscribe(() => {
      this.eventManager.broadcast('informationListModification');
      this.activeModal.close();
    });
  }
}
