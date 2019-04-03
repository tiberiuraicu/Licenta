import { Component, OnInit, EventEmitter, Output } from '@angular/core';
import { Http } from '@angular/http';

@Component({
  selector: 'app-nav-bar',
  templateUrl: './nav-bar.component.html',
  styleUrls: ['./nav-bar.component.scss']
})
export class NavBarComponent implements OnInit {

  constructor(private http: Http) { }
  
  currencies = [];
  ngOnInit() {
    this.http.get("https://api.exchangeratesapi.io/latest").subscribe(response => {
      for (var currency in JSON.parse(response._body).rates)
        this.currencies.push(currency)
    })
  }


  changedValue(selectedValue) {
    this.http.get("https://api.exchangeratesapi.io/latest?base=RON&symbols=" + selectedValue).subscribe(response => {
     localStorage.setItem("globalCurrencyMultiplier",JSON.parse(response._body).rates[selectedValue]);
     localStorage.setItem("globalCurrencyLabel",selectedValue);
    })
 
  }

}
