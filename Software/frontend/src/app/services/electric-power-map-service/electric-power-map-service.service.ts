import { Injectable } from "@angular/core";
import { Http, Headers } from "@angular/http";
import Config from "../../config/config";

@Injectable({
  providedIn: "root"
})
export class ElectricPowerMapServiceService {
  constructor(private http: Http) {}

  //the headers of the authentificated user (without this no resource can be obtained from server)
  userAuthentificationHeader = new Headers({
    Authorization: "Bearer " + localStorage.getItem("token")
  });

  getCircuits = () => {
    return this.http.get(Config.host + "/resources/getCircuits", {
      headers: this.userAuthentificationHeader
    });
  };

  getLocationAndConsumersForCircuit = circuitId => {
    return this.http.post(
      Config.host + "/resources/getLocationAndConsumersForCircuit",
      { circuitId: circuitId },
      { headers: this.userAuthentificationHeader }
    );
  };

  getTodayConsumptionForConsumer = consumerName => {
    return this.http.post(
      Config.host + "/resources/getTodayConsumptionForConsumer",
      { consumerName: consumerName },
      { headers: this.userAuthentificationHeader }
    );
  };

  getStateForConsumers = consumers => {
    return this.http.post(
      Config.host + "/resources/getStateForConsumers",
      consumers,
      { headers: this.userAuthentificationHeader }
    );
  };

  changeSensorState = sensor =>{
    return this.http.post(
      Config.host + "/resources/changeSensorState",
      sensor,
      { headers: this.userAuthentificationHeader }
    );
  }

  changeConsumerState = consumer =>{
    return this.http.post(
      Config.host + "/resources/changeConsumerState",
      consumer,
      { headers: this.userAuthentificationHeader }
    );
  }
}
