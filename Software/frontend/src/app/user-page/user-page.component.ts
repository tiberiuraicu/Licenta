import { Component, OnInit } from "@angular/core";
import { UserService } from "./service/user.service";

@Component({
  selector: "app-user-page",
  templateUrl: "./user-page.component.html",
  styleUrls: ["./user-page.component.scss"]
})
export class UserPageComponent implements OnInit {
  constructor(private userService: UserService) {}

  outlets = [];
  ngOnInit() {
    this.userService.getAllOutlets().subscribe(result => {
      //got response as string
      result._body = result._body.slice(1, -1); //eliminate the "[]" from string
      result._body = result._body.split(" ").join(""); //eliminate the spaces from string
      this.outlets = result._body.split(",");
    });
    this.initializeLineChart(0);
    this.userService.initializePieChart();
    this.userService.initializeWebSocketConnection();
  }

  initializeLineChart(name) {
    this.userService.initializeLineChart(name);
  }
}
