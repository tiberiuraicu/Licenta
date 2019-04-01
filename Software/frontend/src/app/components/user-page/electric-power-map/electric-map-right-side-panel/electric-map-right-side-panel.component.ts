import { Component, OnInit, Input, IterableDiffers } from "@angular/core";
import Chart from "chart.js";
import { ElectricPowerMapServiceService } from "src/app/services/electric-power-map-service/electric-power-map-service.service";

@Component({
  selector: "app-electric-map-right-side-panel",
  templateUrl: "./electric-map-right-side-panel.component.html",
  styleUrls: ["./electric-map-right-side-panel.component.scss"]
})
export class ElectricMapRightSidePanelComponent implements OnInit {
  @Input("cards") cards = [];

  iterableDiffer;
  constructor(
    private _iterableDiffers: IterableDiffers,
  ) {
    this.iterableDiffer = this._iterableDiffers.find([]).create(null);

    
  }

  ngDoCheck() {
    let changes = this.iterableDiffer.diff(this.cards);
    if (changes) {
      setTimeout(() => {
        this.cards.forEach(card => {
          this.makeLineChart(card);
        });
      }, 20);
    }
   
  }

  ngOnInit() {}

  testChange(card) {
    if (card.onOff == "On") card.onOff = "Off";
    if (card.onOff == "Off") card.onOff = "On";
  }
  makeLineChart(card) {
    console.log(card);

    var label;
    if (card.name.includes("sensor")) label = "Triggerd";
    else label = "Last half hour ";
    var data = [];
    data.push(card.value);
    new Chart(document.getElementById(card.name + "line-chart"), {
      type: "bar",
      data: {
        datasets: [
          {
            label: label,
            borderColor: "blue",
            borderWidth: 1,
            fill: false,
            data: data
          }
        ]
      },
      options: {
        scales: {
          xAxes: [
            {
              gridLines: {
                color: "rgba(0, 0, 0, 0)"
              }
            }
          ],
          yAxes: [
            {
              gridLines: {
                color: "rgba(0, 0, 0, 0)"
              }
            }
          ]
        },
        resize: true,
        responsive: true,

        tooltips: {
          mode: "index",
          intersect: true
        },
        annotation: {
          annotations: [
            {
              type: "line",
              mode: "horizontal",
              scaleID: "y-axis-0",
              value: 5,
              borderColor: "#2E2EE5",
              borderWidth: 4,
              label: {
                enabled: false,
                content: "Test label"
              }
            }
          ]
        }
      }
    });
  }
}
