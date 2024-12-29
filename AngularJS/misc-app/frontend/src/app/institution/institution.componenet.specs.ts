import { ComponentFixture, TestBed } from '@angular/core/testing';

import { InstitutionComponent } from './institution.component';

describe('InstitutionComponent', () => {
  let component: InstitutionComponent;
  let fixture: ComponentFixture<InstitutionComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [InstitutionComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(InstitutionComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
