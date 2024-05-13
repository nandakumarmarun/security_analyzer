import { Component, Input } from '@angular/core';
import { RouterModule } from '@angular/router';

import SharedModule from 'app/shared/shared.module';
import { DurationPipe, FormatMediumDatetimePipe, FormatMediumDatePipe } from 'app/shared/date';
import { IUserAttributes } from '../user-attributes.model';

@Component({
  standalone: true,
  selector: 'jhi-user-attributes-detail',
  templateUrl: './user-attributes-detail.component.html',
  imports: [SharedModule, RouterModule, DurationPipe, FormatMediumDatetimePipe, FormatMediumDatePipe],
})
export class UserAttributesDetailComponent {
  @Input() userAttributes: IUserAttributes | null = null;

  previousState(): void {
    window.history.back();
  }
}
