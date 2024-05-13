import { Component, Input } from '@angular/core';
import { RouterModule } from '@angular/router';

import SharedModule from 'app/shared/shared.module';
import { DurationPipe, FormatMediumDatetimePipe, FormatMediumDatePipe } from 'app/shared/date';
import { ICheckLisItem } from '../check-lis-item.model';

@Component({
  standalone: true,
  selector: 'jhi-check-lis-item-detail',
  templateUrl: './check-lis-item-detail.component.html',
  imports: [SharedModule, RouterModule, DurationPipe, FormatMediumDatetimePipe, FormatMediumDatePipe],
})
export class CheckLisItemDetailComponent {
  @Input() checkLisItem: ICheckLisItem | null = null;

  previousState(): void {
    window.history.back();
  }
}
