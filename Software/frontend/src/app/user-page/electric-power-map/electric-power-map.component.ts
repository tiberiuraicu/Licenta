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
        circuit["expanded"] = false;
        circuit["retracted"] = null;
        circuit["hidden"] = false;
        this.circuits.push(circuit);

        console.log(circuit);
      });
    });
  }
  changeSize(circuit) {
    this.circuits.map(mappedCircuit => {
      if (mappedCircuit.circuitId != circuit.circuitId)
        mappedCircuit.retracted = null;
    });
    if (circuit.expanded == false) {
      circuit.retracted = false;
      circuit.expanded = true;
    } else if (circuit.expanded == true) {
      circuit.retracted = true;
      circuit.expanded = false;
     
    }

    this.circuits.map(mappedCircuit => {
      if (mappedCircuit.circuitId != circuit.circuitId)
        mappedCircuit.hidden = !mappedCircuit.hidden;
    });
    this.circuits.map(mappedCircuit => {
      console.log(mappedCircuit);
    });
  
  }
}
