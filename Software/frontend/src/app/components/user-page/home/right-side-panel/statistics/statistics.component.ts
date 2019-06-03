import { Component, OnInit } from '@angular/core';
import { UserService } from 'src/app/services/home-service/home.service';
;

@Component({
  selector: 'app-statistics',
  templateUrl: './statistics.component.html',
  styleUrls: ['./statistics.component.scss']
})
export class StatisticsComponent implements OnInit {
  gaugeType = "arch";
  gaugeValue = 0.5;
  gaugeLabel = "Today Consumption";
  gaugeConsumptionAppendText = "kW";
  gaugeCostAppendText = "";
  animation = false;
  size = 130;
  thick = 6;

  gaugeValueForToday = 0.0;

  gaugeValueForThisMonth = 0.0;

  gaugeCostForToday = 0.0;

  gaugeCostForThisMonth = 0.0;
  constructor(private userService: UserService) {


    setInterval(() => {
      this.gaugeCostForToday =
        Math.round(
          this.gaugeValueForToday *
            0.5 *
            parseFloat(localStorage.getItem("globalCurrencyMultiplier")) *
            100
        ) / 100;

      this.gaugeCostForThisMonth =
        Math.round(
          this.gaugeValueForThisMonth *
            0.5 *
            parseFloat(localStorage.getItem("globalCurrencyMultiplier")) *
            100
        ) / 100;

      this.gaugeCostAppendText = localStorage.getItem("globalCurrencyLabel");
    }, 60);
  }

  ngOnInit() {
    this.getAllConsumedPowerFromHomeForTodayAndThisMonth();
  }

  getAllConsumedPowerFromHomeForTodayAndThisMonth() {

    this.userService
      .getAllConsumedPowerFromHomeForTodayAndThisMonth()
      .subscribe(response => {
      console.log(response)
        this.gaugeValueForToday =
          Math.round(
            (parseFloat(JSON.parse(response._body)[0]["today"])) * 100
          ) / 100;
        this.gaugeValueForThisMonth =
          8* Math.round(
            (parseFloat(JSON.parse(response._body)[0]["thisMonth"])) *
              100
          ) / 100;
      });
  }
}

