import { Injectable } from "@angular/core";
import { Http, Headers } from "@angular/http";
import * as Stomp from "stompjs";
import * as SockJS from "sockjs-client";
import Chart from "chart.js";
import Config from "../../config/config";
import { httpFactory } from "@angular/http/src/http_module";

@Injectable({
  providedIn: "root"
})
export class UserService {
  selectedOutlet: any;
  stompClient;
  pieChart: any;
  lineChart: any;
  constructor(private http: Http) {}

  getHeaders = new Headers({
    Authorization: "Bearer " + localStorage.getItem("token")
  });

  getAllOutlets() {
    return this.http.get("http://localhost:8080/user/resources/getOutlets", {
      headers: this.getHeaders
    });
  }

  initializeLineChart = name => {
    this.selectedOutlet = name;
    let dataName = { name: name };
    let last60RecordsTimestamp = [];
    let last60ResultsPowerConsumed = [];

    this.http
      .post("http://localhost:8080/user/resources/last60Consumers", dataName, {
        headers: this.getHeaders
      })
      .subscribe(result => {
        for (var i in result.json()) {
          last60RecordsTimestamp.push(i.split(" ")[1]);
          last60ResultsPowerConsumed.push(result.json()[i]);
        }

        this.lineChart = new Chart(document.getElementById("line-chart"), {
          type: "line",
          data: {
            labels: last60RecordsTimestamp,
            datasets: [
              {
                label: ["Power Consumed","Timestamp"],
                data: last60ResultsPowerConsumed,
                backgroundColor: ["rgba(255, 99, 132, 0.2)"],
                borderColor: ["rgba(255, 99, 132, 1)"],
                borderWidth: 1
              }
            ]
          },
          options: {
            animation: {
              duration: 10
            },
            scales: {
              yAxes: [
                {
                  scaleLabel: {
                    display: true,
                    labelString: 'Power Consumed'
                  },
                  ticks: {
                    beginAtZero: true
                  }
                }
              ],
              xAxes: [
                {
                  scaleLabel: {
                    display: true,
                    labelString: 'TimeStamp'
                  },
                  ticks: {
                    beginAtZero: true
                  }
                }
              ]
            }
          }
        });
      });
  };

  initializePieChart = () => {
    this.http.get("http://localhost:8080/user/resources/pieChart");
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
  };

  initializeWebSocketConnection = () => {
    const ws = new SockJS(Config.serverSocketURL);
    this.stompClient = Stomp.over(ws);
    let that = this;
    this.stompClient.connect({}, function(frame) {
      that.getTotalPowerconsumedForPieChart();
      that.getOutletPowerConsumedForLineChart();
    });
  };

  getTotalPowerconsumedForPieChart = () => {
    this.stompClient.subscribe("/totalPowerConsumed", response => {
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

  getOutletPowerConsumedForLineChart = () => {
    this.stompClient.subscribe("/outletPowerConsumed", response => {
      var responseAsJson = JSON.parse(response.body);
    for(var outlet in responseAsJson){
      if(outlet==this.selectedOutlet){
        console.log(responseAsJson[outlet]);
        console.log(responseAsJson[outlet[1]]);
         this.addDataToLineChart(responseAsJson[outlet].powerConsumed,responseAsJson[outlet].timestamp.split(" ")[1]);
      }
    }
      // let data = [
      //   powerConsumedFromSolarPanel,
      //   powerConsumedFromNormalPowerSource
      // ];
      //this.addDataToLineChart(data);
    });
  };

  addDataToPieChart(data) {
    this.pieChart.data.datasets[0].data = data;
    this.pieChart.update();
  }

  addDataToLineChart(data,label) {
    this.removeData();
    this.lineChart.data.labels.push(label);
    this.lineChart.data.datasets.forEach(dataset => {
      dataset.data.push(data);
    });
    this.lineChart.update();
  }
   removeData() {
    this.lineChart.data.labels.shift();
    this.lineChart.data.datasets.forEach((dataset) => {
        dataset.data.shift();
    });
    this.lineChart.update();
}
}
