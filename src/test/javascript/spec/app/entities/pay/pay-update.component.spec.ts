import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { of } from 'rxjs';

import { LineTakeoutClientTestModule } from '../../../test.module';
import { PayUpdateComponent } from 'app/entities/pay/pay-update.component';
import { PayService } from 'app/entities/pay/pay.service';
import { Pay } from 'app/shared/model/pay.model';

describe('Component Tests', () => {
  describe('Pay Management Update Component', () => {
    let comp: PayUpdateComponent;
    let fixture: ComponentFixture<PayUpdateComponent>;
    let service: PayService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [LineTakeoutClientTestModule],
        declarations: [PayUpdateComponent],
        providers: [FormBuilder],
      })
        .overrideTemplate(PayUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(PayUpdateComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(PayService);
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', fakeAsync(() => {
        // GIVEN
        const entity = new Pay(123);
        spyOn(service, 'update').and.returnValue(of(new HttpResponse({ body: entity })));
        comp.updateForm(entity);
        // WHEN
        comp.save();
        tick(); // simulate async

        // THEN
        expect(service.update).toHaveBeenCalledWith(entity);
        expect(comp.isSaving).toEqual(false);
      }));

      it('Should call create service on save for new entity', fakeAsync(() => {
        // GIVEN
        const entity = new Pay();
        spyOn(service, 'create').and.returnValue(of(new HttpResponse({ body: entity })));
        comp.updateForm(entity);
        // WHEN
        comp.save();
        tick(); // simulate async

        // THEN
        expect(service.create).toHaveBeenCalledWith(entity);
        expect(comp.isSaving).toEqual(false);
      }));
    });
  });
});
