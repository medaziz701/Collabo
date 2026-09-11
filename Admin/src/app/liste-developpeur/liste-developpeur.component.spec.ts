import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ListeDeveloopeurComponent } from './liste-developpeur.component';

describe('ListeDeveloopeurComponent', () => {
  let component: ListeDeveloopeurComponent;
  let fixture: ComponentFixture<ListeDeveloopeurComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [ListeDeveloopeurComponent]
    });
    fixture = TestBed.createComponent(ListeDeveloopeurComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
