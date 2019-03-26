import { Component, OnInit } from "@angular/core";
import { ElectricPowerMapServiceService } from "../service/electric-power-map-service/electric-power-map-service.service";
import { Observable, Subscriber, of } from "rxjs";
import { tap, map, filter } from "rxjs/operators";

@Component({
  selector: "app-electric-power-map",
  templateUrl: "./electric-power-map.component.html",
  styleUrls: ["./electric-power-map.component.scss"]
})
export class ElectricPowerMapComponent implements OnInit {
  constructor(
    private electricPowerMapServiceService: ElectricPowerMapServiceService
  ) {}
  circuits = [];
  ngOnInit() {
    this.electricPowerMapServiceService.getCircuits().subscribe(response => {
      
        JSON.parse(response._body).forEach(circuit => {
          this.circuits.push(circuit)
        });
    });
  }
  
}
