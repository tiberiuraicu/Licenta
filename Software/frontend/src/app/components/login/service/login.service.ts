import { Injectable } from '@angular/core';
import { Http, Headers } from '@angular/http';
import { Observable } from 'rxjs';
import { Router } from '@angular/router';


@Injectable({
  providedIn: 'root'
})
export class LoginService {
  token: string;

  constructor(private http: Http, private router : Router) { }

  sendCredential(model) {
    let tokenUrl = "http://localhost:8080/user/login";
    let headers = new Headers({ 'Content-Type': 'application/json' });
    return this.http.post(tokenUrl, JSON.stringify(model), { headers: headers })
  }

  sendToken(token) {
    let tokenUrl = "http://localhost:8080/resources/users";
    console.log('Bearer ' + token);
    let getHeaders = new Headers({ 'Authorization': 'Bearer ' + token });
    return this.http.get(tokenUrl, { headers: getHeaders });
  }

  getUserId(token) {
    let tokenUrl = "http://localhost:8080/user/getId";
    console.log('Bearer ' + token);
    let getHeaders = new Headers({ 'Authorization': 'Bearer ' + token });
    return this.http.post(tokenUrl,localStorage.getItem("currentEmail"), { headers: getHeaders });
  }

  logout() {
    localStorage.setItem("token", "");
    localStorage.setItem("currentEmail", "")
    this.router.navigate(['/home'])
    location.reload();
    alert("you just logged out.")
  }

  // checkLogin() {
  //   if (localStorage.getItem("currentEmail") != null && localStorage.getItem("currentEmail") != '' && localStorage.getItem("token") != null && localStorage.getItem("token") != '') {
    
  //     return true;
  //   }
  //   else {
  //     return false;
  //   }
  // }

  send() {
    this.token = localStorage.getItem("token");
    let tokenUrl = "http://localhost:8080/rest1/";
    console.log('Bearer ' + this.token);

    let getHeaders = new Headers({ 'Atuhorization': 'Bearer ' + this.token });

    return this.http.get(tokenUrl, { headers: getHeaders }).subscribe(
      data => {
        console.log(data);
      });
  }


}
