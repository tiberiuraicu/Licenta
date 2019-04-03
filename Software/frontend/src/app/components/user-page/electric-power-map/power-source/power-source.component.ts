import { Component, OnInit, Input, OnChanges, SimpleChanges } from '@angular/core';
import { Http } from '@angular/http';

@Component({
  selector: 'app-power-source',
  templateUrl: './power-source.component.html',
  styleUrls: ['./power-source.component.scss']
})
export class PowerSourceComponent implements OnChanges, OnInit {

  @Input("circuit") circuit;

  constructor() {}
  globalCurrencyLabel;
  moneySaved = this.circuit == undefined ? 0 : this.circuit.powerSourceGeneratedPower * 0.5 * parseFloat(localStorage.getItem("globalCurrencyMultiplier"));

  ngOnInit() {

  }

  ngOnChanges(changes: SimpleChanges): void {
    console.log(this.circuit )
    this.moneySaved = this.circuit == undefined ? 0 : changes.circuit.currentValue.powerSourceGeneratedPower * 0.5 * parseFloat(localStorage.getItem("globalCurrencyMultiplier"));
    console.log(  this.moneySaved)
    this.globalCurrencyLabel=localStorage.getItem("globalCurrencyLabel");
  }
}
