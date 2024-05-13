import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import { IState } from '../state.model';
import { StateService } from '../service/state.service';

@Component({
  standalone: true,
  templateUrl: './state-delete-dialog.component.html',
  imports: [SharedModule, FormsModule],
})
export class StateDeleteDialogComponent {
  state?: IState;

  protected stateService = inject(StateService);
  protected activeModal = inject(NgbActiveModal);

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.stateService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
