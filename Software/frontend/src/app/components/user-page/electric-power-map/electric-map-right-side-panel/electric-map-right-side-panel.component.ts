import { Component, OnInit, Input, IterableDiffers } from "@angular/core";
import Chart from "chart.js";
import { ElectricPowerMapServiceService } from "src/app/services/electric-power-map-service/electric-power-map-service.service";
import { cardEnter } from "src/app/animations/electric-power-map.animations";

@Component({
  selector: "app-electric-map-right-side-panel",
  templateUrl: "./electric-map-right-side-panel.component.html",
  styleUrls: ["./electric-map-right-side-panel.component.scss"],
  animations:[cardEnter]
})
export class ElectricMapRightSidePanelComponent implements OnInit {
  cards = [];

  iterableDiffer;
  constructor(
    private _iterableDiffers: IterableDiffers,
    private electricPowerMapServiceService: ElectricPowerMapServiceService
  ) {
    this.iterableDiffer = this._iterableDiffers.find([]).create(null);
  }

  ngDoCheck() {
    let changes = this.iterableDiffer.diff(this.cards);
    if (changes) {
      setTimeout(() => {
        this.cards.forEach(card => {
          card["animationState"]="visible";
          console.log(card)
        
          this.makeLineChart(card);
        });
      }, 20);
    }
  }

  ngOnInit() {
    
      this.electricPowerMapServiceService
      .getStateForConsumers()
      .subscribe(response => {
        this.cards = JSON.parse(response._body);
        console.log(this.cards)
      });
   }


  makeLineChart(card) {

    var label;
    if (card.name.includes("sensor")) label = "Triggerd";
    else label = "Ultima jumatate de oră ";
    var data = [];
    data.push(card.value);
    new Chart(document.getElementById(card.name + "line-chart"), {
      type: "bar",
      data: {
        datasets: [
          {
            label: label,
            borderColor: "#E8175D",
            borderWidth: 1,
            fill: false,
            data: data,
            fillStyle: "white",
          }
        ]
      },
      options: {
        legend: {
          labels: {
            fontColor: '#ffffff'
         }
      },
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
