import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import { ICheckLisItem } from '../check-lis-item.model';
import { CheckLisItemService } from '../service/check-lis-item.service';

@Component({
  standalone: true,
  templateUrl: './check-lis-item-delete-dialog.component.html',
  imports: [SharedModule, FormsModule],
})
export class CheckLisItemDeleteDialogComponent {
  checkLisItem?: ICheckLisItem;

  protected checkLisItemService = inject(CheckLisItemService);
  protected activeModal = inject(NgbActiveModal);

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.checkLisItemService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
