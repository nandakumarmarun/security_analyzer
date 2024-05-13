import { Component, Input } from '@angular/core';
import { RouterModule } from '@angular/router';

import SharedModule from 'app/shared/shared.module';
import { DurationPipe, FormatMediumDatetimePipe, FormatMediumDatePipe } from 'app/shared/date';
import { ICheckList } from '../check-list.model';

@Component({
  standalone: true,
  selector: 'jhi-check-list-detail',
  templateUrl: './check-list-detail.component.html',
  imports: [SharedModule, RouterModule, DurationPipe, FormatMediumDatetimePipe, FormatMediumDatePipe],
})
export class CheckListDetailComponent {
  @Input() checkList: ICheckList | null = null;

  previousState(): void {
    window.history.back();
  }
}
