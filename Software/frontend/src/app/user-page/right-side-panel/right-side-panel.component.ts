import { Component, OnInit } from '@angular/core';
import * as Stomp from "stompjs";
import * as SockJS from "sockjs-client";
import Chart from "chart.js";
import Config from "../../config/config";
@Component({
  selector: 'app-right-side-panel',
  templateUrl: './right-side-panel.component.html',
  styleUrls: ['./right-side-panel.component.scss']
})
export class RightSidePanelComponent implements OnInit {

  constructor() { }
  notifications=[];
  ngOnInit() {
    this.initializeWebSocketConnection()
  }
  stompClient;
  initializeWebSocketConnection = () => {
    const ws = new SockJS(Config.serverSocketURL);
    this.stompClient = Stomp.over(ws);
    let that = this;
    this.stompClient.connect({}, function(frame) {
      that.getNotifications();
     
    });
  };

  getNotifications = () => {
    this.stompClient.subscribe("/notification", response => {
this.notifications.push(response.body);
     console.log(response);
    });
  };
}
