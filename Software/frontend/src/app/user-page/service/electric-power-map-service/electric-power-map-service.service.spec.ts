import { TestBed } from '@angular/core/testing';

import { ElectricPowerMapServiceService } from './electric-power-map-service.service';

describe('ElectricPowerMapServiceService', () => {
  beforeEach(() => TestBed.configureTestingModule({}));

  it('should be created', () => {
    const service: ElectricPowerMapServiceService = TestBed.get(ElectricPowerMapServiceService);
    expect(service).toBeTruthy();
  });
});
