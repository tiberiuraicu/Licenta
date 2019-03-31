import { Injectable } from "@angular/core";
import { Http,Headers } from "@angular/http";

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
    return this.http.get("http://localhost:8080/resources/getCircuits",{headers:this.userAuthentificationHeader});
  };


  getCircuitsForTreeMap = (circuitId) => {
    return this.http.post("http://localhost:8080/resources/getCircuitsForMapPage",{circuitId:circuitId},{headers:this.userAuthentificationHeader});
  };

  getTodayConsumptionForConsumer = (consumerName) => {
    return this.http.post("http://localhost:8080/resources/getTodayConsumptionForConsumer",{consumerName:consumerName},{headers:this.userAuthentificationHeader});
  };

  getStateForConsumers = (consumers) => {
    return this.http.post("http://localhost:8080/resources/getStateForConsumers",consumers,{headers:this.userAuthentificationHeader});
  };
}
