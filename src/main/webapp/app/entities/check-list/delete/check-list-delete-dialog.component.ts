import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import { ICheckList } from '../check-list.model';
import { CheckListService } from '../service/check-list.service';

@Component({
  standalone: true,
  templateUrl: './check-list-delete-dialog.component.html',
  imports: [SharedModule, FormsModule],
})
export class CheckListDeleteDialogComponent {
  checkList?: ICheckList;

  protected checkListService = inject(CheckListService);
  protected activeModal = inject(NgbActiveModal);

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.checkListService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
