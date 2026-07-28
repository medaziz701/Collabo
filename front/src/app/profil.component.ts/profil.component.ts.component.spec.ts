import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ProfilComponentTsComponent } from './profil.component.ts.component';

describe('ProfilComponentTsComponent', () => {
  let component: ProfilComponentTsComponent;
  let fixture: ComponentFixture<ProfilComponentTsComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [ProfilComponentTsComponent]
    });
    fixture = TestBed.createComponent(ProfilComponentTsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
