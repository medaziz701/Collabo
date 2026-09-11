import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ListeContactComponent } from './liste-contact.component';

describe('ListeContactComponent', () => {
  let component: ListeContactComponent;
  let fixture: ComponentFixture<ListeContactComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [ListeContactComponent]
    });
    fixture = TestBed.createComponent(ListeContactComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
