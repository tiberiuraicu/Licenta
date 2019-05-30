import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { ElectronicDeviceCardComponent } from './electronic-device-card.component';

describe('ElectronicDeviceCardComponent', () => {
  let component: ElectronicDeviceCardComponent;
  let fixture: ComponentFixture<ElectronicDeviceCardComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ ElectronicDeviceCardComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(ElectronicDeviceCardComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
