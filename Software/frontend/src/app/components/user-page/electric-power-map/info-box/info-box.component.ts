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
  constructor() {}

  ngOnInit() {}

}
