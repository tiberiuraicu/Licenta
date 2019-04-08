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
  gaugeAppendText = "W";
  animation = true;
  size = 130;
  thick=6

  
  gaugeValueForToday = 0.0;
  gaugeLabelForToday = "Today consumption";

  gaugeValueForThisMonth = 0.0;
  gaugeLabelForThisMonth = "This month";

  constructor(private userService: UserService) {
    this.getAllConsumedPowerFromHomeForToday();
    this.getAllConsumedPowerFromHomeForThisMonth();
  }

  notifications = [];

  ngOnInit() {
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
      console.log(response);
    });
  };

  getAllConsumedPowerFromHomeForToday() {
    setInterval(() => {
      this.userService
        .getAllConsumedPowerFromHomeForToday()
        .subscribe(response => {
          this.gaugeValueForToday = response._body;
        });
    }, 1000);
  }
  getAllConsumedPowerFromHomeForThisMonth() {
    setInterval(() => {
      this.userService
        .getAllConsumedPowerFromHomeForThisMonth()
        .subscribe(response => {
          this.gaugeValueForThisMonth = response._body;
        });
    }, 1000);
  }
}
