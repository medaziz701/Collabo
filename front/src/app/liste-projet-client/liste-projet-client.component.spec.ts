import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ListeProjetClientComponent } from './liste-projet-client.component';

describe('ListeProjetClientComponent', () => {
  let component: ListeProjetClientComponent;
  let fixture: ComponentFixture<ListeProjetClientComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [ListeProjetClientComponent]
    });
    fixture = TestBed.createComponent(ListeProjetClientComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
