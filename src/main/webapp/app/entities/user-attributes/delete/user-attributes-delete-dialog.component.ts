import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import { IUserAttributes } from '../user-attributes.model';
import { UserAttributesService } from '../service/user-attributes.service';

@Component({
  standalone: true,
  templateUrl: './user-attributes-delete-dialog.component.html',
  imports: [SharedModule, FormsModule],
})
export class UserAttributesDeleteDialogComponent {
  userAttributes?: IUserAttributes;

  protected userAttributesService = inject(UserAttributesService);
  protected activeModal = inject(NgbActiveModal);

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.userAttributesService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
