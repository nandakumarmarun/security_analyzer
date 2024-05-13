import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import { ISecurityTest } from '../security-test.model';
import { SecurityTestService } from '../service/security-test.service';

@Component({
  standalone: true,
  templateUrl: './security-test-delete-dialog.component.html',
  imports: [SharedModule, FormsModule],
})
export class SecurityTestDeleteDialogComponent {
  securityTest?: ISecurityTest;

  protected securityTestService = inject(SecurityTestService);
  protected activeModal = inject(NgbActiveModal);

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.securityTestService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
