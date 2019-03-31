import { Injectable } from '@angular/core';
import { Http, Headers } from '@angular/http';

@Injectable({
  providedIn: 'root'
})
export class RegisterService {

  constructor(private http: Http) { }

  register(model) {
    let registerUrl = "http://localhost:8080/user/register";
    let headers = new Headers({ 'Content-Type': 'application/json' });
    return this.http.post(registerUrl, JSON.stringify(model), { headers: headers })
  }
}
