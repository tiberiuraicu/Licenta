import { Component, OnInit } from "@angular/core";
import { UserService } from "./service/user.service";
import {NestedTreeControl} from '@angular/cdk/tree';
import { ArrayDataSource } from '@angular/cdk/collections';


interface FoodNode {
    name: string;
    children?: FoodNode[];
  }
  
  const TREE_DATA: FoodNode[] = [
    {
      name: 'Fruit',
      children: [
        {name: 'Apple'},
        {name: 'Banana'},
        {name: 'Fruit loops'},
      ]
    }, {
      name: 'Vegetables',
      children: [
        {
          name: 'Green',
          children: [
            {name: 'Broccoli'},
            {name: 'Brussel sprouts'},
          ]
        }, {
          name: 'Orange',
          children: [
            {name: 'Pumpkins'},
            {name: 'Carrots'},
          ]
        },
      ]
    },
  ];
  
  /**
   * @title Tree with nested nodes
   */



@Component({
  selector: "app-user-page",
  templateUrl: "./user-page.component.html",
  styleUrls: ["./user-page.component.scss"]
})
export class UserPageComponent implements OnInit {
  constructor(private userService: UserService) {}
  treeControl = new NestedTreeControl<FoodNode> (node => node.children);
  dataSource = new ArrayDataSource(TREE_DATA);

  hasChild = (_: number, node: FoodNode) => !!node.children && node.children.length > 0;
  outlets = [];
  ngOnInit() {
   
    this.userService.getAllOutlets().subscribe(result => {
      //got response as string

      console.log(JSON.parse(result._body));
     
    
    });
    this.initializeLineChart(0);
    this.userService.initializePieChart();
    this.userService.startOutletBroadcast();
    this.userService.initializeWebSocketConnection();
  }

  initializeLineChart(name) {
    this.userService.initializeLineChart(name);
  }
}
