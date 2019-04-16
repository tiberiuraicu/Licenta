import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { LoginService } from './service/login.service';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.scss'],
  animations: [

  ]
})
export class LoginComponent {
  private info;
   model = { 'email': '', 'password': '' }
  private currentEmail;

  constructor(private loginService: LoginService, private router : Router) {
    this.currentEmail = localStorage.getItem("currentEmail");

  }

  onSubmit() {
    this.loginService.sendCredential(this.model).subscribe(
      data => {
       
        localStorage.setItem("token", data._body)
        localStorage.setItem("currentEmail", this.model.email);
        this.loginService.getUserId(data._body).subscribe(response=>{
          localStorage.setItem("currentId", response._body);
        })
         this.router.navigate(['/user/home'])
         //location.reload();
      },
      error => console.log(error)
    );
  }


}
