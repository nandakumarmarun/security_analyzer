import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import { ITestCheckList } from '../test-check-list.model';
import { TestCheckListService } from '../service/test-check-list.service';

@Component({
  standalone: true,
  templateUrl: './test-check-list-delete-dialog.component.html',
  imports: [SharedModule, FormsModule],
})
export class TestCheckListDeleteDialogComponent {
  testCheckList?: ITestCheckList;

  protected testCheckListService = inject(TestCheckListService);
  protected activeModal = inject(NgbActiveModal);

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.testCheckListService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
