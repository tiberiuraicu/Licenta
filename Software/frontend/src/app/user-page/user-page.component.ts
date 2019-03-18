import { Component, OnInit } from "@angular/core";
import { UserService } from "./service/user.service";
import * as Stomp from "stompjs";
import * as SockJS from "sockjs-client";
import Chart from "chart.js";
import Config from "../config/config";

@Component({
  selector: "app-user-page",
  templateUrl: "./user-page.component.html",
  styleUrls: ["./user-page.component.scss"]
})
export class UserPageComponent implements OnInit {
  constructor(private userService: UserService) {}
  pieChart: any;
  lineChart: any;
  ngOnInit() {
    this.initializeWebSocketConnection();

    this.pieChart = new Chart(document.getElementById("doughnut-chart"), {
      type: "doughnut",
      data: {
        labels: ["Solar Panel", "Normal Power Source"],
        datasets: [
          {
            label: "Consumption(kW)",
            backgroundColor: ["#3e95cd", "#F7464A"],
            data: [5, 5]
          }
        ]
      },
      options: {
        animation: {
          duration: 0
        },
        title: {
          display: true,
          text: "Power consumption"
        }
      }
    });

    this.lineChart=new Chart(document.getElementById("line-chart"), {
      type: "line",
      data: {
        labels: ["Red", "Blue", "Yellow", "Green", "Purple", "Orange"],
        datasets: [
          {
            label: "# of Votes",
            data: [12, 19, 3, 5, 2, 3],
            backgroundColor: [
              "rgba(255, 99, 132, 0.2)",
              "rgba(54, 162, 235, 0.2)",
              "rgba(255, 206, 86, 0.2)",
              "rgba(75, 192, 192, 0.2)",
              "rgba(153, 102, 255, 0.2)",
              "rgba(255, 159, 64, 0.2)"
            ],
            borderColor: [
              "rgba(255, 99, 132, 1)",
              "rgba(54, 162, 235, 1)",
              "rgba(255, 206, 86, 1)",
              "rgba(75, 192, 192, 1)",
              "rgba(153, 102, 255, 1)",
              "rgba(255, 159, 64, 1)"
            ],
            borderWidth: 1
          }
        ]
      },
      options: {
        scales: {
          yAxes: [
            {
              ticks: {
                beginAtZero: true
              }
            }
          ]
        }
      }
    });
  }
  initializeWebSocketConnection = () => {
    const ws = new SockJS(Config.serverSocketURL);
    const stompClient = Stomp.over(ws);
    let that = this;
    stompClient.connect({}, function(frame) {
      that.getTotalPowerconsumed(stompClient);
      that.getOutletPowerConsumed(stompClient);
    });
  };

  getTotalPowerconsumed = stompClient => {
    stompClient.subscribe("/totalPowerConsumed", response => {
      var responseAsJson = JSON.parse(response.body);
      let powerConsumedFromSolarPanel =
        responseAsJson.powerConsumedFromSolarPanel;
      let powerConsumedFromNormalPowerSource =
        responseAsJson.powerConsumedFromNormalPowerSource;
      let data = [
        powerConsumedFromSolarPanel,
        powerConsumedFromNormalPowerSource
      ];
      this.addDataToPieChart(data);
    });
  };
  getOutletPowerConsumed = stompClient => {
    stompClient.subscribe("/outletPowerConsumed", response => {
      var responseAsJson = JSON.parse(response.body);
     console.log(responseAsJson);
      // let data = [
      //   powerConsumedFromSolarPanel,
      //   powerConsumedFromNormalPowerSource
      // ];
      //this.addDataToLineChart(data);
    });
  };

  initializeChart = () => {};
  // sendMessage(message) {
  //   this.stompClient.send("/app/topic/message", {}, "message");
  //   $("#input").val("");
  // }
  addDataToPieChart(data) {
    this.pieChart.data.datasets[0].data = data;
    this.pieChart.update();
  }
   addDataToLineChart( data) {
    //this.lineChart.data.labels.push(label);
    this.lineChart.data.datasets.forEach((dataset) => {
        dataset.data.push(data);
    });
    this.lineChart.update();
}
}
