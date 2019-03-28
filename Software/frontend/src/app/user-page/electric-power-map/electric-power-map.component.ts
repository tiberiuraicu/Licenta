import { Component, OnInit, ViewEncapsulation } from "@angular/core";
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
import * as d3 from "d3";

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
          transform: "translate(calc(0%), calc(5%))",
          marginTop: "1%",
          width: "75%",
          float: "left",
          marginLeft: "10%",
          height: "70%"
        })
      ),
      transition("retracted <=> expanded", animate(2000))
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
  ],
  encapsulation: ViewEncapsulation.None
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
      this.treex();
    });
  }

  // changeSize(circuit) {
  //   circuit.currentState =
  //     circuit.currentState === "retracted" ? "expanded" : "retracted";

  //   this.circuits.map(mappedCircuit => {
  //     if (
  //       circuit.circuitId != mappedCircuit.circuitId &&
  //       circuit.currentState === "retracted"
  //     )
  //       mappedCircuit.hidden = "visible";
  //     if (
  //       circuit.circuitId != mappedCircuit.circuitId &&
  //       circuit.currentState === "expanded"
  //     )
  //       mappedCircuit.hidden = "hidden";
  //       console.log(mappedCircuit)
  //   });

  // }
  // changeX(event){

  //     let offsetLeft = 0;
  //     let offsetTop = 0;

  //     let el = event.srcElement;

  //     while(el){
  //         offsetLeft += el.offsetLeft;
  //         offsetTop += el.offsetTop;
  //         el = el.parentElement;
  //     }
  //     console.log(offsetTop , offsetLeft )
  // }

  treex() {
    var treeData = {
      name: "Top Level",
      children: [
        {
          name: "Level 2: A",
          children: [{ name: "Son of A" }, { name: "Daughter of A" }]
        },
        { name: "Level 2: B" }
      ]
    };

    // set the dimensions and margins of the diagram
    var margin = { top: 40, right: 90, bottom: 50, left: 90 },
      width = 660 - margin.left - margin.right,
      height = 500 - margin.top - margin.bottom;

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
        .select("body")
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

    // adds the text to the node
    node
      .append("text")
      .attr("dy", ".35em")
      .attr("y", function(d) {
        return d.children ? -20 : 20;
      })
      .style("text-anchor", "middle")
      .text(function(d) {
        return d.data.name;
      });
  }
 
}
