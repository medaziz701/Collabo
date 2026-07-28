import { ComponentFixture, TestBed } from '@angular/core/testing';

import { AccueilDeveloppeurComponent } from './accueil-developpeur.component';

describe('AccueilDeveloppeurComponent', () => {
  let component: AccueilDeveloppeurComponent;
  let fixture: ComponentFixture<AccueilDeveloppeurComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [AccueilDeveloppeurComponent]
    });
    fixture = TestBed.createComponent(AccueilDeveloppeurComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
