import { Component, OnInit } from "@angular/core";
import * as Stomp from "stompjs";
import * as SockJS from "sockjs-client";
import Config, { SocketConfig } from "../../../../config/config";
import { UserService } from "src/app/services/home-service/home.service";
@Component({
  selector: "app-right-side-panel",
  templateUrl: "./right-side-panel.component.html",
  styleUrls: ["./right-side-panel.component.scss"]
})
export class RightSidePanelComponent implements OnInit {
  gaugeType = "arch";
  gaugeValue = 0.5;
  gaugeLabel = "Today Consumption";
  gaugeConsumptionAppendText = "kW";
  gaugeCostAppendText = "";
  animation = true;
  size = 130;
  thick = 6;

  gaugeValueForToday = 0.0;

  gaugeValueForThisMonth = 0.0;

  gaugeCostForToday = 0.0;

  gaugeCostForThisMonth = 0.0;

  constructor(private userService: UserService) {
   

    setInterval(() => {
      this.gaugeCostForToday =
        Math.round(
          this.gaugeValueForToday *
            0.5 *
            parseFloat(localStorage.getItem("globalCurrencyMultiplier")) *
            100
        ) / 100;

      this.gaugeCostForThisMonth =
        Math.round(
          this.gaugeValueForThisMonth *
            0.5 *
            parseFloat(localStorage.getItem("globalCurrencyMultiplier")) *
            100
        ) / 100;

      this.gaugeCostAppendText = localStorage.getItem("globalCurrencyLabel");
    }, 60);
  }

  notifications = [];

  ngOnInit() {
    this.getAllConsumedPowerFromHomeForTodayAndThisMonth();
    this.initializeWebSocketConnection();
  }
  stompClient;
  initializeWebSocketConnection = () => {
    const ws = new SockJS(SocketConfig.serverSocketURL);
    this.stompClient = Stomp.over(ws);
    let that = this;
    this.stompClient.connect({}, function(frame) {
      that.getNotifications();
    });
  };

  getNotifications = () => {
    let userID = localStorage.getItem("currentId");
    this.stompClient.subscribe("/notification/" + userID, response => {
      this.notifications.push(response.body);
     
    });
  };

  getAllConsumedPowerFromHomeForTodayAndThisMonth() {
      
    
    this.userService
      .getAllConsumedPowerFromHomeForTodayAndThisMonth()
      .subscribe(response => {
        console.log("--------------------------------------------------------------------------------------------------------------------------------------")

        console.log(response)
        this.gaugeValueForToday =
          Math.round(
            (parseFloat(JSON.parse(response._body)[0]["today"]) / 1000) * 100
          ) / 100;
        this.gaugeValueForThisMonth =
          Math.round(
            (parseFloat(JSON.parse(response._body)[0]["thisMonth"]) / 1000) *
              100
          ) / 100;
      });
     
  }
}
