import { Component, OnInit } from "@angular/core";
import { Router } from "@angular/router";
import { LoginService } from "./service/login.service";

@Component({
  selector: "app-login",
  templateUrl: "./login.component.html",
  styleUrls: ["./login.component.scss"],
  animations: []
})
export class LoginComponent {
  userNotExisting;
  private info;
  model = { email: "", password: "" };
  private currentEmail;

  constructor(private loginService: LoginService, private router: Router) {
    this.currentEmail = localStorage.getItem("currentEmail");
  }

  onSubmit() {
    localStorage.setItem("token", "");
    localStorage.setItem("currentEmail", "");
    localStorage.setItem("currentId", "");

    this.loginService.sendCredential(this.model).subscribe(
      data => {
        localStorage.setItem("token", data._body);
        localStorage.setItem("currentEmail", this.model.email);
        this.loginService.getUserId(data._body).subscribe(response => {
          localStorage.setItem("currentId", response._body);
        });
        if (localStorage.getItem("token") != "nu exista")
          setTimeout(() => {
            this.router.navigate(["/user/home"]);
          }, 50);
        else this.userNotExisting = true;
      },
      error => console.log(error)
    );
  }
}
