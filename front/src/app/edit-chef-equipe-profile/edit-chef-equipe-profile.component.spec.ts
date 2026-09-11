import { ComponentFixture, TestBed } from '@angular/core/testing';

import { EditChefEquipeProfileComponent } from './edit-chef-equipe-profile.component';

describe('EditChefEquipeProfileComponent', () => {
  let component: EditChefEquipeProfileComponent;
  let fixture: ComponentFixture<EditChefEquipeProfileComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [EditChefEquipeProfileComponent]
    });
    fixture = TestBed.createComponent(EditChefEquipeProfileComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
