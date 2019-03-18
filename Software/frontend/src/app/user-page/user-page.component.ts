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

      result._body = result._body.slice(1, -1);
      result._body=result._body.split(' ').join('');
      this.outlets = result._body.split(",");

      
    });
    this.initializeLineChart(0)
    this.userService.initializePieChart();
    this.userService.initializeWebSocketConnection();
   
  }

  initializeLineChart(name){
    this.userService.initializeLineChart(name);
  }
}
