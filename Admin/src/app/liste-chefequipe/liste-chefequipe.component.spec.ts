import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ListeChefequipeComponent } from './liste-chefequipe.component';

describe('ListeChefequipeComponent', () => {
  let component: ListeChefequipeComponent;
  let fixture: ComponentFixture<ListeChefequipeComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [ListeChefequipeComponent]
    });
    fixture = TestBed.createComponent(ListeChefequipeComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
