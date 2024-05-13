import { Component, Input } from '@angular/core';
import { RouterModule } from '@angular/router';

import SharedModule from 'app/shared/shared.module';
import { DurationPipe, FormatMediumDatetimePipe, FormatMediumDatePipe } from 'app/shared/date';
import { ITestCheckLisItem } from '../test-check-lis-item.model';

@Component({
  standalone: true,
  selector: 'jhi-test-check-lis-item-detail',
  templateUrl: './test-check-lis-item-detail.component.html',
  imports: [SharedModule, RouterModule, DurationPipe, FormatMediumDatetimePipe, FormatMediumDatePipe],
})
export class TestCheckLisItemDetailComponent {
  @Input() testCheckLisItem: ITestCheckLisItem | null = null;

  previousState(): void {
    window.history.back();
  }
}
