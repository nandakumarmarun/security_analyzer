import { Component, Input } from '@angular/core';
import { RouterModule } from '@angular/router';

import SharedModule from 'app/shared/shared.module';
import { DurationPipe, FormatMediumDatetimePipe, FormatMediumDatePipe } from 'app/shared/date';
import { ITestCheckList } from '../test-check-list.model';

@Component({
  standalone: true,
  selector: 'jhi-test-check-list-detail',
  templateUrl: './test-check-list-detail.component.html',
  imports: [SharedModule, RouterModule, DurationPipe, FormatMediumDatetimePipe, FormatMediumDatePipe],
})
export class TestCheckListDetailComponent {
  @Input() testCheckList: ITestCheckList | null = null;

  previousState(): void {
    window.history.back();
  }
}
