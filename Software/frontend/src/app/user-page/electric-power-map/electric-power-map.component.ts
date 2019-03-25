import { Component, OnInit, ViewChild, ElementRef } from '@angular/core';
import { Surface } from '@progress/kendo-drawing';

import { drawScene } from './draw-scene';

@Component({
  selector: 'app-electric-power-map',
  templateUrl: './electric-power-map.component.html',
  styleUrls: ['./electric-power-map.component.scss']
})
export class ElectricPowerMapComponent implements OnInit {

  constructor() { }

  ngOnInit() {
    
  }
  @ViewChild('surface')
  private surfaceElement: ElementRef;
  private surface: Surface;

  public ngAfterViewInit(): void {
    drawScene(this.createSurface());
  }

  public ngOnDestroy() {
    this.surface.destroy();
  }

  private createSurface(): Surface {
    // Obtain a reference to the native DOM element of the wrapper
    const element = this.surfaceElement.nativeElement;

    // Create a drawing surface
    this.surface = Surface.create(element);

    return this.surface;
  }

}
