import { Component, OnInit } from '@angular/core';
import { RegisterService } from './service/register.service';

@Component({
  selector: 'app-register',
  templateUrl: './register.component.html',
  styleUrls: ['./register.component.scss']
})
export class RegisterComponent implements OnInit {

 model = { 'email': '', 'password': '', 'deviceId': '','firstName': '', 'lastName': '', 'phoneNumber': '', 'country': '', 'locality': '' }
  constructor(private registerService : RegisterService) { }

  register() {
    this.registerService.register(this.model).subscribe(data=>{
      console.log(data);
    })
  }

  ngOnInit() {
  }

}
