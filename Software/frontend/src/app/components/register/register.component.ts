import { Component, OnInit } from '@angular/core';
import { RegisterService } from './service/register.service';

@Component({
  selector: 'app-register',
  templateUrl: './register.component.html',
  styleUrls: ['./register.component.scss']
})
export class RegisterComponent implements OnInit {

  private model = { 'email': '', 'password': '', 'deviceId': '' }
  constructor(private registerService : RegisterService) { }

  register() {
    this.registerService.register(this.model).subscribe(data=>{
      console.log(data);
    })
  }

  ngOnInit() {
  }

}
