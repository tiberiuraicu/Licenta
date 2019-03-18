import { Injectable, OnInit } from "@angular/core";
import { Http, Headers } from "@angular/http";
import * as Stomp from "stompjs";
import * as SockJS from "sockjs-client";
import Chart from "chart.js";
import Config from "../../config/config";

@Injectable({
  providedIn: "root"
})

export class UserService {
 
  constructor(private http: Http) {}

  // getHeaders = new Headers({
  //   Authorization: "Bearer " + localStorage.getItem("token")
  // });

  // getTotalPowerConsumed = () => {
  //  return  this.http
  //     .get("http://localhost:8080/user/resources/totalPowerConsumed", {
  //       headers: this.getHeaders
  //     })

  // };

}
