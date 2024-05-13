import { Component, Input } from '@angular/core';
import { RouterModule } from '@angular/router';

import SharedModule from 'app/shared/shared.module';
import { DurationPipe, FormatMediumDatetimePipe, FormatMediumDatePipe } from 'app/shared/date';
import { ISecurityTest } from '../security-test.model';

@Component({
  standalone: true,
  selector: 'jhi-security-test-detail',
  templateUrl: './security-test-detail.component.html',
  imports: [SharedModule, RouterModule, DurationPipe, FormatMediumDatetimePipe, FormatMediumDatePipe],
})
export class SecurityTestDetailComponent {
  @Input() securityTest: ISecurityTest | null = null;

  previousState(): void {
    window.history.back();
  }
}
