import { Component, Input } from '@angular/core';
import { RouterModule } from '@angular/router';

import SharedModule from 'app/shared/shared.module';
import { DurationPipe, FormatMediumDatetimePipe, FormatMediumDatePipe } from 'app/shared/date';
import { IState } from '../state.model';

@Component({
  standalone: true,
  selector: 'jhi-state-detail',
  templateUrl: './state-detail.component.html',
  imports: [SharedModule, RouterModule, DurationPipe, FormatMediumDatetimePipe, FormatMediumDatePipe],
})
export class StateDetailComponent {
  @Input() state: IState | null = null;

  previousState(): void {
    window.history.back();
  }
}
