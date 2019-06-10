import { Component, OnInit, Input } from '@angular/core';
import { ElectricPowerMapServiceService } from 'src/app/services/electric-power-map-service/electric-power-map-service.service';
import { delay } from 'q';
import Config, { SocketConfig } from "../../../../../config/config";
import * as Stomp from "stompjs";
import * as SockJS from "sockjs-client";

@Component({
  selector: 'app-electronic-device-card',
  templateUrl: './electronic-device-card.component.html',
  styleUrls: ['./electronic-device-card.component.scss']
})
export class ElectronicDeviceCardComponent implements OnInit {
  powerConsumedUnderTreshold = false;
  @Input() card;
  constructor(private electricPowerMapService: ElectricPowerMapServiceService
  ) { }

  ngOnInit() {

    delay(50)
    var consumerName;
    if (this.card.name.includes("Priza"))
      consumerName = this.card.name.replace("Priza ", "outlet");
    if (this.card.name.includes("Întrerupător"))
      consumerName = this.card.name.replace("Întrerupător ", "switch")

    this.initializeWebSocketConnection(consumerName);

  }


  changeState(card) {
    var consumerName;
    if (card.name.includes("Priza"))
      consumerName = card.name.replace("Priza ", "outlet");
    if (card.name.includes("Întrerupător"))
      consumerName = card.name.replace("Întrerupător ", "switch")
    var state;
    if (card.state) {
      state = 0;
      card.state = false;
    }
    else if (!card.state) {
      state = 1;
      card.state = true;
    }

    // if (card.name.includes("outlet"))
    if (card.name.includes("Priza"))
      this.electricPowerMapService
        .changeConsumerState({ state: state, name: consumerName })
        .subscribe(response => {
          console.log(response);
        });

    // if ( card.name.includes("switch"))

    this.electricPowerMapService
      .changeConsumerState({ state: state, name: consumerName })
      .subscribe(response => {
        this.showConfirmationOnInterface(response)
      });
  }
  showConfirmationOnInterface(response: import("@angular/http").Response) {
    throw new Error("Method not implemented.");
  }

  stompClient;
  initializeWebSocketConnection = (consumerName) => {
    const ws = new SockJS(SocketConfig.serverSocketURL);
    this.stompClient = Stomp.over(ws);
    let that = this;
    this.stompClient.connect({}, function (frame) {
      that.getConsumerState(consumerName);
      that.getConsumerLastPowerConsumedValue(consumerName);
    });
  };
  getConsumerState = (consumerName) => {

    this.stompClient.subscribe("/state/" + consumerName, response => {
      console.log(response.body)
      if (response.body == "0") {

        this.card.state = false;
        this.powerConsumedUnderTreshold = false;

      }
      else if (response.body == "1") {

        this.card.state = true;
      }
    });
  };

  getConsumerLastPowerConsumedValue = (consumerName) => {

    this.stompClient.subscribe("/powerConsumed/" + consumerName, response => {
      console.log(response.body)
      if (response.body > 0.44) {

        this.powerConsumedUnderTreshold = false;
      }
      else if (response.body < 0.44) {

        this.powerConsumedUnderTreshold = true;
      }

    });
  };
}
