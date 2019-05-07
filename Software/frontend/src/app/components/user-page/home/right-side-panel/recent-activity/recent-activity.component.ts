import { Component, OnInit } from '@angular/core';
import * as Stomp from "stompjs";
import * as SockJS from "sockjs-client";
import Config, { SocketConfig } from "../../../../../config/config";
import { UserService } from "src/app/services/home-service/home.service";
@Component({
  selector: 'app-recent-activity',
  templateUrl: './recent-activity.component.html',
  styleUrls: ['./recent-activity.component.scss']
})
export class RecentActivityComponent implements OnInit {

  constructor() { }

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
     
    });
  };

}
