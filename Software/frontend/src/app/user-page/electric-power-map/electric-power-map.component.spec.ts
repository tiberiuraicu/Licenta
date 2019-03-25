import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { ElectricPowerMapComponent } from './electric-power-map.component';

describe('ElectricPowerMapComponent', () => {
  let component: ElectricPowerMapComponent;
  let fixture: ComponentFixture<ElectricPowerMapComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ ElectricPowerMapComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(ElectricPowerMapComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
