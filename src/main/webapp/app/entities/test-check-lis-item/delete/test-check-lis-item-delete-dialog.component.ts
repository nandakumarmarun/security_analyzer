import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import { ITestCheckLisItem } from '../test-check-lis-item.model';
import { TestCheckLisItemService } from '../service/test-check-lis-item.service';

@Component({
  standalone: true,
  templateUrl: './test-check-lis-item-delete-dialog.component.html',
  imports: [SharedModule, FormsModule],
})
export class TestCheckLisItemDeleteDialogComponent {
  testCheckLisItem?: ITestCheckLisItem;

  protected testCheckLisItemService = inject(TestCheckLisItemService);
  protected activeModal = inject(NgbActiveModal);

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.testCheckLisItemService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
