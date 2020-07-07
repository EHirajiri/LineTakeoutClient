import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { LineTakeoutClientTestModule } from '../../../test.module';
import { PayDetailComponent } from 'app/entities/pay/pay-detail.component';
import { Pay } from 'app/shared/model/pay.model';

describe('Component Tests', () => {
  describe('Pay Management Detail Component', () => {
    let comp: PayDetailComponent;
    let fixture: ComponentFixture<PayDetailComponent>;
    const route = ({ data: of({ pay: new Pay(123) }) } as any) as ActivatedRoute;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [LineTakeoutClientTestModule],
        declarations: [PayDetailComponent],
        providers: [{ provide: ActivatedRoute, useValue: route }],
      })
        .overrideTemplate(PayDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(PayDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load pay on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.pay).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});
