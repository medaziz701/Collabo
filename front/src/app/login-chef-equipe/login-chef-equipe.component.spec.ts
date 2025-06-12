import { ComponentFixture, TestBed } from '@angular/core/testing';

import { LoginChefEquipeComponent } from './login-chef-equipe.component';

describe('LoginChefEquipeComponent', () => {
  let component: LoginChefEquipeComponent;
  let fixture: ComponentFixture<LoginChefEquipeComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [LoginChefEquipeComponent]
    });
    fixture = TestBed.createComponent(LoginChefEquipeComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
