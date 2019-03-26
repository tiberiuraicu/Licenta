import { Injectable } from "@angular/core";
import { Http, Headers } from "@angular/http";
import * as Stomp from "stompjs";
import * as SockJS from "sockjs-client";
import Chart from "chart.js";
import Config from "../../../config/config";

@Injectable({
  providedIn: "root"
})
export class UserService {
  selectedOutlet: any;
  stompClient;
  pieChart: any;
  lineChart: any;

  constructor(private http: Http) {}

  // the headers of the authentificated user (without this no resource can be obtained from server)
  userAuthentificationHeader = new Headers({
    Authorization: "Bearer " + localStorage.getItem("token")
  });

  // getting all existing outlets from server
  getAllOutlets() {
    return this.http.get("http://localhost:8080/user/resources/getOutlets", {
      headers: this.userAuthentificationHeader
    });
  }
  startOutletBroadcast() {
    this.http
      .post(
        "http://localhost:8080/user/resources/lineChart",
        { userId: localStorage.getItem("currentId") },
        {
          headers: this.userAuthentificationHeader
        }
      )
      .subscribe(res => {});
  }

  // initialize the line chart with information fron server about the selected outlet
  initializeLineChart = name => {
    // set the global selected outlet
    this.selectedOutlet = name;

    let last60RecordsTimestamp = [];
    let last60RecordsPowerConsumed = [];

    //get the last 60 records of outlet and initialize the line chart
    //make the request to server
    this.http
      .post(
        "http://localhost:8080/user/resources/last60Consumers",
        { outletName: name, userId: localStorage.getItem("currentId") },
        { headers: this.userAuthentificationHeader }
      )
      .subscribe(response => {
        // iterate trough response (its form: {'timestamp':'.......','powerConsumed': '..........'})
        for (var outletData in response.json()) {
          //get the timestamp
          var thisOutletRecordTimestamp = outletData.split(" ")[1]; //just the hour
          //add it to the list
          last60RecordsTimestamp.push(thisOutletRecordTimestamp);
          //get the power consumed
          var thisOutletRecordPowerConsumed = response.json()[outletData];
          //add it to the list
          last60RecordsPowerConsumed.push(thisOutletRecordPowerConsumed);
        }

        this.lineChart = new Chart(document.getElementById("line-chart"), {
          type: "line",
          data: {
            labels: last60RecordsTimestamp,
            datasets: [
              {
                label: "Outlet power consumed",
                data: last60RecordsPowerConsumed,
                backgroundColor: ["rgba(255, 99, 132, 0.2)"],
                borderColor: ["rgba(255, 99, 132, 1)"],
                borderWidth: 1
              }
            ]
          },
          options: {
            resize: true,
            animation: {
              duration: 10
            },
            scales: {
              yAxes: [
                {
                  scaleLabel: {
                    display: true,
                    labelString: "Power Consumed"
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
                    labelString: "TimeStamp"
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
    this.http
      .post(
        "http://localhost:8080/user/resources/pieChart",
        { userId: localStorage.getItem("currentId") },
        { headers: this.userAuthentificationHeader }
      )
      .subscribe(res => {});
    this.pieChart = new Chart(document.getElementById("doughnut-chart"), {
      type: "doughnut",
      data: {
        labels: ["Solar Panel", "Normal Power Source"],
        datasets: [
          {
            label: "Consumption(kW)",
            backgroundColor: ["#3e95cd", "#F7464A"],
            data: [0, 0]
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
    let userID = localStorage.getItem("currentId");
    this.stompClient.subscribe("/totalPowerConsumed/" + userID, response => {
      console.log(response);
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
    let userID = localStorage.getItem("currentId");
    this.stompClient.subscribe("/outletPowerConsumed/" + userID, response => {
      console.log(response);
      var responseAsJson = JSON.parse(response.body);
      for (var outlet in responseAsJson) {
        if (outlet == this.selectedOutlet) {
          var lastPowerConsumedForOutlet = responseAsJson[outlet].powerConsumed;
          var lastRecordedTimestampForOutlet = responseAsJson[
            outlet
          ].timestamp.split(" ")[1]; //just the hour
          this.addDataToLineChart(
            lastPowerConsumedForOutlet,
            lastRecordedTimestampForOutlet
          );
        }
      }
    });
  };

  addDataToPieChart(data) {
    this.pieChart.data.datasets[0].data = data;
    this.pieChart.update();
  }

  addDataToLineChart(data, label) {
    this.removeData();
    this.lineChart.data.labels.push(label);
    this.lineChart.data.datasets.forEach(dataset => {
      dataset.data.push(data);
    });
    this.lineChart.update();
  }
  removeData() {
    this.lineChart.data.labels.shift();
    this.lineChart.data.datasets.forEach(dataset => {
      dataset.data.shift();
    });
    this.lineChart.update();
  }
}
