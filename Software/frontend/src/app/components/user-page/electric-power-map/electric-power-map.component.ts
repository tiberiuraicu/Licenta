import { Component, OnInit, ViewEncapsulation, ViewChild } from "@angular/core";
import * as $ from "jquery";
import * as d3 from "d3";
import { ElectricPowerMapServiceService } from "src/app/services/electric-power-map-service/electric-power-map-service.service";
import { extend, fade } from "src/app/animations/electric-power-map.animations";
import { PowerSourceComponent } from "./power-source/power-source.component";
import { InfoBoxComponent } from "./info-box/info-box.component";

@Component({
  selector: "app-electric-power-map",
  templateUrl: "./electric-power-map.component.html",
  styleUrls: ["./electric-power-map.component.scss"],
  animations: [extend, fade],
  encapsulation: ViewEncapsulation.None
})
export class ElectricPowerMapComponent implements OnInit {
  constructor(
    private electricPowerMapServiceService: ElectricPowerMapServiceService
  ) {}

  @ViewChild(PowerSourceComponent) powerSourceComponent: PowerSourceComponent;
  @ViewChild(InfoBoxComponent) infoBoxComponent: InfoBoxComponent;

  selectedCircuit;
  infoBoxIcon;
  name;
  lastHourConsumption;
  todayConsumption;
  circuits = [];
  circuitChildren = [];
  infoBox = { circuit: false, location: false, consumer: false };

  ngOnInit() {
    this.electricPowerMapServiceService.getCircuits().subscribe(response => {
      JSON.parse(response._body).forEach(circuit => {
        circuit["currentState"] = "retracted";
        circuit["hidden"] = "visible";
        circuit["offsetTop"] = 5;
        this.circuits.push(circuit);
      });
    });
  }

  showInfo(node) {
    if (node.depth == 0) {
    } else if (node.depth == 1) {
    } else if (node.depth == 2) {
      this.infoBox.consumer = true;
      if (node.data.name.includes("Priză"))
        this.infoBoxIcon = "./assets/resources/outlet.png";
      if (node.data.name.includes("Senzor"))
        this.infoBoxIcon = "./assets/resources/sensor.png";
      if (node.data.name.includes("Întrerupător"))
        this.infoBoxIcon = "./assets/resources/switch.png";
      var nume;
      if (node.data.name.includes("Priză"))
        nume=node.data.name.replace("Priză ","outlet")
      if (node.data.name.includes("Senzor"))
      nume=node.data.name.replace("Senzor ","sensor")
      if (node.data.name.includes("Întrerupător"))
      nume=node.data.name.replace("Întrerupător ","switch")

      this.electricPowerMapServiceService
        .getTodayConsumptionForConsumer(nume)
        .subscribe(response => {
          let consumerInfo = JSON.parse(response._body);
          this.lastHourConsumption = Math.round(consumerInfo["lastHour"]);
          this.todayConsumption = Math.round(consumerInfo["today"]);
          this.name = node.data.name;
          this.infoBoxComponent.startCurrencyMonitoring();
        });
    }
  }
  changeSize(circuit) {
    this.selectedCircuit = circuit;

    if (circuit.currentState === "retracted") {
      circuit.currentState = "expanded";
      this.electricPowerMapServiceService
        .getLocationAndConsumersForCircuit(circuit.circuitId)
        .subscribe(response => {
          let treeData = JSON.parse(response._body);

          this.createTree(treeData, circuit.circuitId);

          this.powerSourceComponent.startCurrencyMonitoring();
        });
    } else {
      circuit.currentState = "retracted";
      $("svg").remove();
      for (var element in this.infoBox) {
        this.infoBox[element] = false;
      }
    }

    this.circuits.map(mappedCircuit => {
      console.log(this.circuits);
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
    });
  }
  repositionCircuit(circuit) {
    circuit.offsetTop = document.getElementById(
      "circuit" + circuit.circuitId
    ).offsetTop;
  }

  createTree = (treeData, circuitId) => {
    // set the dimensions and margins of the diagram
    var margin = { top: 140, right: 140, bottom: 50, left: 20 },
      width = 1020 - margin.left - margin.right,
      height = 550 - margin.top - margin.bottom;

    // declares a tree layout and assigns the size
    var treemap = d3.tree().size([width, height]);

    //  assigns the data to a hierarchy using parent-child relationships
    var nodes = d3.hierarchy(treeData);

    // maps the node data to the tree layout
    nodes = treemap(nodes);

    // append the svg obgect to the body of the page
    // appends a 'group' element to 'svg'
    // moves the 'group' element to the top left margin
    var svg = d3
        .select("#circuit" + circuitId)
        .append("svg")
        .attr("width", width + margin.left + margin.right)
        .attr("height", height + margin.top + margin.bottom),
      g = svg
        .append("g")
        .attr("transform", "translate(" + margin.left + "," + margin.top + ")");

    // adds the links between the nodes
    var link = g
      .selectAll(".link")
      .data(nodes.descendants().slice(1))
      .enter()
      .append("path")
      .attr("class", "link")
      .attr("d", function(d) {
        return (
          "M" +
          d.x +
          "," +
          d.y +
          "C" +
          d.x +
          "," +
          (d.y + d.parent.y) / 2 +
          " " +
          d.parent.x +
          "," +
          (d.y + d.parent.y) / 2 +
          " " +
          d.parent.x +
          "," +
          d.parent.y
        );
      });

    // adds each node as a group
    var node = g
      .selectAll(".node")
      .data(nodes.descendants())
      .enter()
      .append("g")
      .attr("class", function(d) {
        return "node" + (d.children ? " node--internal" : " node--leaf");
      })
      .attr("transform", function(d) {
        return "translate(" + d.x + "," + d.y + ")";
      });

    // adds the circle to the node
    node.append("circle").attr("r", 10);

    node.on("click", this.showInfo.bind(this));
    // adds the text to the node
    node
      .append("text")
      .attr("dy", ".35em")
      .attr("y", function(d) {
        return d.children ? -20 : 20;
      })
      .style("text-anchor", "middle")
      .style("fill", "white")
      .text(function(d) {
        return d.data.name;
      });
  };
}
