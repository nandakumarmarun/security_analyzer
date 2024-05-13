import { ComponentFixture, TestBed } from '@angular/core/testing';
import { provideRouter, withComponentInputBinding } from '@angular/router';
import { RouterTestingHarness, RouterTestingModule } from '@angular/router/testing';
import { of } from 'rxjs';

import { TestCheckLisItemDetailComponent } from './test-check-lis-item-detail.component';

describe('TestCheckLisItem Management Detail Component', () => {
  let comp: TestCheckLisItemDetailComponent;
  let fixture: ComponentFixture<TestCheckLisItemDetailComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [TestCheckLisItemDetailComponent, RouterTestingModule.withRoutes([], { bindToComponentInputs: true })],
      providers: [
        provideRouter(
          [
            {
              path: '**',
              component: TestCheckLisItemDetailComponent,
              resolve: { testCheckLisItem: () => of({ id: 123 }) },
            },
          ],
          withComponentInputBinding(),
        ),
      ],
    })
      .overrideTemplate(TestCheckLisItemDetailComponent, '')
      .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(TestCheckLisItemDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load testCheckLisItem on init', async () => {
      const harness = await RouterTestingHarness.create();
      const instance = await harness.navigateByUrl('/', TestCheckLisItemDetailComponent);

      // THEN
      expect(instance.testCheckLisItem).toEqual(expect.objectContaining({ id: 123 }));
    });
  });

  describe('PreviousState', () => {
    it('Should navigate to previous state', () => {
      jest.spyOn(window.history, 'back');
      comp.previousState();
      expect(window.history.back).toHaveBeenCalled();
    });
  });
});
