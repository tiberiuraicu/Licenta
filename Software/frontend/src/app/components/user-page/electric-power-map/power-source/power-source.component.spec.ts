import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { PowerSourceComponent } from './power-source.component';

describe('PowerSourceComponent', () => {
  let component: PowerSourceComponent;
  let fixture: ComponentFixture<PowerSourceComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ PowerSourceComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(PowerSourceComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
