import { ComponentFixture, TestBed } from '@angular/core/testing';

import { AccueilChefEquipeComponent } from './accueil-chef-equipe.component';

describe('AccueilChefEquipeComponent', () => {
  let component: AccueilChefEquipeComponent;
  let fixture: ComponentFixture<AccueilChefEquipeComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [AccueilChefEquipeComponent]
    });
    fixture = TestBed.createComponent(AccueilChefEquipeComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
