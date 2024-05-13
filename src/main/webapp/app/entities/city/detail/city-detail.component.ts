import { Component, Input } from '@angular/core';
import { RouterModule } from '@angular/router';

import SharedModule from 'app/shared/shared.module';
import { DurationPipe, FormatMediumDatetimePipe, FormatMediumDatePipe } from 'app/shared/date';
import { ICity } from '../city.model';

@Component({
  standalone: true,
  selector: 'jhi-city-detail',
  templateUrl: './city-detail.component.html',
  imports: [SharedModule, RouterModule, DurationPipe, FormatMediumDatetimePipe, FormatMediumDatePipe],
})
export class CityDetailComponent {
  @Input() city: ICity | null = null;

  previousState(): void {
    window.history.back();
  }
}
