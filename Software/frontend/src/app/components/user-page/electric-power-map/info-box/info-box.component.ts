import { Component, OnInit, Input } from "@angular/core";

@Component({
  selector: "app-info-box",
  templateUrl: "./info-box.component.html",
  styleUrls: ["./info-box.component.scss"]
})
export class InfoBoxComponent implements OnInit {
  @Input("infoBox") infoBox;
  @Input("icon") icon;
  @Input("name") name;
  @Input("lastHourConsumption") lastHourConsumption;
  @Input("todayConsumption") todayConsumption;
  interval;
  value = {
    currency:'RON',
    lastHourConsumptionMoneyValue: 0,
    todayConsumptionMoneyValue: 0
  }
  constructor() { }

  ngOnInit() { }

  startCurrencyMonitoring() {
    this.interval = setInterval(() => {

      if (this.infoBox.consumer==false) {
        clearInterval(this.interval);

      }
      this.value.lastHourConsumptionMoneyValue = this.lastHourConsumption == undefined ? 0 : Math.round(this.lastHourConsumption * 0.5 * parseFloat(localStorage.getItem("globalCurrencyMultiplier")) * 100) / 100;
      this.value.currency = localStorage.getItem("globalCurrencyLabel");
      this.value.todayConsumptionMoneyValue = this.todayConsumption == undefined ? 0 : Math.round(this.todayConsumption * 0.5 * parseFloat(localStorage.getItem("globalCurrencyMultiplier")) * 100) / 100;
      this.value.currency = localStorage.getItem("globalCurrencyLabel");
    }, 60)

  }
}
