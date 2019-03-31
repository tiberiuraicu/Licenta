import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { ElectricMapRightSidePanelComponent } from './electric-map-right-side-panel.component';

describe('ElectricMapRightSidePanelComponent', () => {
  let component: ElectricMapRightSidePanelComponent;
  let fixture: ComponentFixture<ElectricMapRightSidePanelComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ ElectricMapRightSidePanelComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(ElectricMapRightSidePanelComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
