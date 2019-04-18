import { Injectable } from "@angular/core";
import { Http, Headers } from "@angular/http";
import Chart from "chart.js";
import Config from "../../config/config";

@Injectable({
  providedIn: "root"
})
export class UserService {
  selectedOutlet: any;
  pieChart: any;
  lineChart: any;

  pieChartRequestInterval: any;
  lineChartRequestInterval: any;

  constructor(private http: Http) { }

  // the headers of the authentificated user (without this no resource can be obtained from server)
  userAuthentificationHeader = new Headers({
    Authorization: "Bearer " + localStorage.getItem("token")
  });

  // getting all existing outlets from server
  getAllOutlets() {
    return this.http.get(Config.host + "/resources/getOutlets", {
      headers: this.userAuthentificationHeader
    });
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
        Config.host + "/resources/last60Consumers",
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
                backgroundColor: ["rgba(248, 249, 250, 0.2)"],
                borderColor: ["rgba(60, 148, 150, 1)"],

                borderWidth: 2
              }
            ]
          },
          options: {
            legend: { labels: { fontColor: "rgba(248, 249, 250, 0.9)" } },
            resize: true,
            animation: {
              duration: 10
            },
            scales: {
              yAxes: [
                {
                  scaleLabel: {
                    fontColor: "rgba(248, 249, 250, 0.9)",
                    display: true,
                    labelString: "Power Consumed"
                  },
                  ticks: {
                    beginAtZero: true,
                    fontColor: "rgba(248, 249, 250, 0.9)"
                  }
                }
              ],
              xAxes: [
                {
                  scaleLabel: {
                    display: true,
                    labelString: "TimeStamp",
                    fontColor: "rgba(248, 249, 250, 0.9)"
                  },
                  ticks: {
                    beginAtZero: true,
                    fontColor: "rgba(248, 249, 250, 0.9)"
                  }
                }
              ]
            }
          }
        });
      });
   
  };

  initializePieChart = () => {

    let userID = localStorage.getItem("currentId");
    // this.stompClient.subscribe("/totalPowerConsumed/" + userID, response => {
    //   console.log(response);
    this.http
      .post(
        Config.host + "/resources/getLastRecordForPieChart",
        { userId: localStorage.getItem("currentId") },
        { headers: this.userAuthentificationHeader }
      )
      .subscribe(response => {
        console.log(response)
        var responseAsJson = JSON.parse(response._body);
        let powerConsumedFromSolarPanel =
          responseAsJson.powerConsumedFromSolarPanel;
        let powerConsumedFromNormalPowerSource =
          responseAsJson.powerConsumedFromNormalPowerSource;
        let data = [
          powerConsumedFromSolarPanel,
          powerConsumedFromNormalPowerSource
        ];

        //});
        this.pieChart = new Chart(document.getElementById("doughnut-chart"), {
          type: "doughnut",
          data: {
            labels: ["Solar Panel", "Normal Power Source"],
            datasets: [
              {
                label: "Consumption(kW)",
                backgroundColor: ["rgba(62, 149, 205, 1)", "rgba(247, 70, 74, 1)"],
                data: data,
                borderColor: "rgba(248, 249, 250, 0.4)",

              }
            ]
          },
          options: {
            legend: {
              labels: {
                fontColor: '#ffffff'
              }
            },
            animation: {
              duration: 0
            },
            title: {
              display: true,
              text: "Power consumption",
              fontColor: "rgba(248, 249, 250, 0.9)"
            }
          }
        });
      });

    this.getTotalPowerconsumedForPieChart();


  };



  getTotalPowerconsumedForPieChart = () => {
    this.pieChartRequestInterval = setInterval(() => {
      let userID = localStorage.getItem("currentId");
      // this.stompClient.subscribe("/totalPowerConsumed/" + userID, response => {
      //   console.log(response);
      this.http
        .post(
          Config.host + "/resources/getLastRecordForPieChart",
          { userId: localStorage.getItem("currentId") },
          { headers: this.userAuthentificationHeader }
        )
        .subscribe(response => {
          console.log(response)
          var responseAsJson = JSON.parse(response._body);
          let powerConsumedFromSolarPanel =
            responseAsJson.powerConsumedFromSolarPanel;
          let powerConsumedFromNormalPowerSource =
            responseAsJson.powerConsumedFromNormalPowerSource;
          let data = [
            powerConsumedFromSolarPanel,
            powerConsumedFromNormalPowerSource
          ];
          this.addDataToPieChart(data);
          //});
        });
    }, 1000)
  };

  getOutletPowerConsumedForLineChart = () => {
    this.lineChartRequestInterval = setInterval(() => {
      let userID = localStorage.getItem("currentId");
      this.http
        .post(
          Config.host + "/resources/getLastRecordOfEveryOutlet",
          { userId: localStorage.getItem("currentId") },
          { headers: this.userAuthentificationHeader }
        )
        .subscribe(response => {
          console.log(response);
          console.log("---------------------------------------------------------------------")
          var responseAsJson = JSON.parse(response._body);
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
    }, 1000);
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

  getAllConsumedPowerFromHomeForTodayAndThisMonth() {

    return this.http.get(Config.host + "/resources/getAllConsumedPowerFromHomeForTodayAndThisMonth", {
      headers: this.userAuthentificationHeader
    });
  }
}
