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

  @Input() card;
  constructor(private electricPowerMapServiceService: ElectricPowerMapServiceService
  ) { }

  ngOnInit() {
 
   

    delay(50)
    var consumerName;
    if (this.card.name.includes("Priza"))
      consumerName = this.card.name.replace("Priza ", "outlet");
    if (this.card.name.includes("Întrerupător"))
      consumerName = this.card.name.replace("Întrerupător ", "switch")
    console.log(consumerName)
    this.initializeWebSocketConnection(consumerName);
    
    // setInterval(() => {
    //   this.electricPowerMapServiceService.getConsumerState(consumerName).subscribe(response => {
    //     console.log(response)
    //     if (response._body == "0") {

    //       this.card.state = false;
    //     }
    //     else if (response._body == "1") {

    //       this.card.state = true;
    //     }
    //   })
    // }, 500)

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
      this.electricPowerMapServiceService
        .changeConsumerState({ state: state, name: consumerName })
        .subscribe(response => {
          console.log(response);
        });

    // if ( card.name.includes("switch"))

    this.electricPowerMapServiceService
      .changeConsumerState({ state: state, name: consumerName })
      .subscribe(response => {
        console.log(response);
      });
  }






  stompClient;
  initializeWebSocketConnection = (consumerName) => {
    const ws = new SockJS(SocketConfig.serverSocketURL);
    this.stompClient = Stomp.over(ws);
    let that = this;
    this.stompClient.connect({}, function(frame) {
      that.getNotifications(consumerName);
    });
  };
  getNotifications = (consumerName) => {

    this.stompClient.subscribe("/state/" + consumerName, response => {
console.log(response)
      if (response.body == "0") {

              this.card.state = false;
            }
            else if (response.body == "1") {
    
              this.card.state = true;
            }
      
     
    });
  };
}
