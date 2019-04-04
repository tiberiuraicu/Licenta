import { Component, OnInit, Input, OnChanges, SimpleChanges } from '@angular/core';



@Component({
  selector: 'app-power-source',
  templateUrl: './power-source.component.html',
  styleUrls: ['./power-source.component.scss']
})
export class PowerSourceComponent implements OnInit {


  @Input("circuit") circuit;
  interval;
  constructor() { }
  globalCurrencyLabel;
  powerSourceType;
  moneyWorth = this.circuit == undefined ? 0 : Math.round(this.circuit.powerSourceGeneratedPower * 0.5 * parseFloat(localStorage.getItem("globalCurrencyMultiplier")) * 100) / 100;

  ngOnInit() {
  }

  startCurrencyMonitoring() {
    if (this.circuit.powerSource == "solarPanel")
      this.powerSourceType = "Solar panel";
    else if (this.circuit.powerSource == "normalPowerSource")
      this.powerSourceType = "Standard power supply";
    var powerSourceGeneratedPower = this.circuit.powerSourceGeneratedPower;
    this.interval = setInterval(() => {

      if (this.circuit.currentState == "retracted") {
        clearInterval(this.interval);

      }
      this.moneyWorth = this.circuit == undefined ? 0 : Math.round(powerSourceGeneratedPower * 0.5 * parseFloat(localStorage.getItem("globalCurrencyMultiplier")) * 100) / 100;
      this.globalCurrencyLabel = localStorage.getItem("globalCurrencyLabel");
    }, 60)

  }
}
