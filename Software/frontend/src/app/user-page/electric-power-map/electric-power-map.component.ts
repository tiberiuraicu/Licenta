import { Component, OnInit } from "@angular/core";
import { ElectricPowerMapServiceService } from "../service/electric-power-map-service/electric-power-map-service.service";
import { Observable, Subscriber, of } from "rxjs";
import { tap, map, filter } from "rxjs/operators";
import {
  trigger,
  transition,
  animate,
  style,
  state
} from "@angular/animations";

@Component({
  selector: "app-electric-power-map",
  templateUrl: "./electric-power-map.component.html",
  styleUrls: ["./electric-power-map.component.scss"],
  animations: [
    trigger("fade", [
      state(
        "retracted",
        style({
          marginTop: "1%",
          width: "75%",
          float: "left",
          marginLeft: "10%",
          height: "10%"
        })
      ),
      state(
        "expanded",
        style({
          transform:'translate(calc(0%), calc(5%))',
          marginTop: "1%",
          width: "75%",
          float: "left",
          marginLeft: "10%",
          height: "70%" 
       
        })
      ),
      transition("retracted <=> expanded", animate(2000)),
    
  
    ]),
    trigger("hidden", [
      state(
        "hidden",
        style({
          opacity: 0
        })
      ),
      state(
        "visible",
        style({
          opacity: 1
        })
      ),
      transition("hidden => visible", animate(2000)),
      transition("visible => hidden", animate(2000))
    ])
  ]
})
export class ElectricPowerMapComponent implements OnInit {
  constructor(
    private electricPowerMapServiceService: ElectricPowerMapServiceService
  ) {}

  circuits = [];

  ngOnInit() {
    this.electricPowerMapServiceService.getCircuits().subscribe(response => {
      JSON.parse(response._body).forEach(circuit => {
        circuit["currentState"] = "retracted";
        circuit["hidden"] = "visible";
        this.circuits.push(circuit);

        console.log(circuit);
      });
    });
  }

  changeSize(circuit) {
    circuit.currentState =
      circuit.currentState === "retracted" ? "expanded" : "retracted";

    this.circuits.map(mappedCircuit => {
      if (
        circuit.circuitId != mappedCircuit.circuitId &&
        circuit.currentState === "retracted"
      )
        mappedCircuit.hidden = "visible";
      if (
        circuit.circuitId != mappedCircuit.circuitId &&
        circuit.currentState === "expanded"
      )
        mappedCircuit.hidden = "hidden";
        console.log(mappedCircuit)
    });

  }
  changeX(event){
    console.log(event)
  }
}
