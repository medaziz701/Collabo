import { ComponentFixture, TestBed } from '@angular/core/testing';

import { AjouterChefEquipeComponent } from './ajouter-chef-equipe.component';

describe('AjouterChefEquipeComponent', () => {
  let component: AjouterChefEquipeComponent;
  let fixture: ComponentFixture<AjouterChefEquipeComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [AjouterChefEquipeComponent]
    });
    fixture = TestBed.createComponent(AjouterChefEquipeComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
