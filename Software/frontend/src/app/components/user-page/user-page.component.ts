import { Component, OnInit, OnDestroy } from "@angular/core";
import { UserService } from "src/app/services/home-service/home.service";
import { Router } from "@angular/router";
import { LoginService } from '../login/service/login.service';

@Component({
  selector: "app-user-page",
  templateUrl: "./user-page.component.html",
  styleUrls: ["./user-page.component.scss"]
})
export class UserPageComponent implements OnInit, OnDestroy {
  interval;
  ngOnDestroy(): void {
    clearInterval(this.interval);
  }
  constructor(private userServices: UserService, private router: Router,private loginService : LoginService) {}
  ngOnInit() {
     if (localStorage.getItem("token") == "") this.router.navigate(["/login"]);
    this.interval = setInterval(() => {
      this.userServices.checkIfTokenValid().subscribe(
        response => {},
        error => {
          this.loginService.logout();
          clearInterval(this.interval);
        }
      );
    }, 500);
  }
}
