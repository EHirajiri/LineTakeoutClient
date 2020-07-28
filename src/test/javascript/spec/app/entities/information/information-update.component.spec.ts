import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { of } from 'rxjs';

import { LineTakeoutClientTestModule } from '../../../test.module';
import { InformationUpdateComponent } from 'app/entities/information/information-update.component';
import { InformationService } from 'app/entities/information/information.service';
import { Information } from 'app/shared/model/information.model';

describe('Component Tests', () => {
  describe('Information Management Update Component', () => {
    let comp: InformationUpdateComponent;
    let fixture: ComponentFixture<InformationUpdateComponent>;
    let service: InformationService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [LineTakeoutClientTestModule],
        declarations: [InformationUpdateComponent],
        providers: [FormBuilder],
      })
        .overrideTemplate(InformationUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(InformationUpdateComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(InformationService);
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', fakeAsync(() => {
        // GIVEN
        const entity = new Information(123);
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
        const entity = new Information();
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
