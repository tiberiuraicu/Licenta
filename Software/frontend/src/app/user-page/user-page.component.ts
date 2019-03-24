import { Component, OnInit } from "@angular/core";
import { UserService } from "./service/user.service";
import { NestedTreeControl } from "@angular/cdk/tree";
import { ArrayDataSource } from "@angular/cdk/collections";
import { TreeNode } from '@angular/router/src/utils/tree';

interface Location {
  name: string;
  children?: Outlet[];
}
interface Outlet{
  name:string
}

interface FoodNode {
  name: string;
  children?: FoodNode[];
}

const TREE_DATA: FoodNode[] = [
  {
    name: "Fruit",
    children: [{ name: "Apple" }, { name: "Banana" }, { name: "Fruit loops" }]
  },
  {
    name: "Vegetables",
    children: [
      {
        name: "Green",
        children: [{ name: "Broccoli" }, { name: "Brussel sprouts" }]
      },
      {
        name: "Orange",
        children: [{ name: "Pumpkins" }, { name: "Carrots" }]
      }
    ]
  }
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

  treeControl = new NestedTreeControl<FoodNode>(node => node.children);
  tree_data=[];
  dataSource ;

  hasChild = (_: number, node: FoodNode) =>
    !!node.children && node.children.length > 0;

 
  ngOnInit() {
    this.userService.getAllOutlets().subscribe(result => {
      //got response as string

      console.log(JSON.parse(result._body));

      for (var locatio in JSON.parse(result._body)) {
        

        var array : Outlet[]=[];
        for (var outlet in JSON.parse(result._body)[locatio]) {
          console.log(JSON.parse(result._body)[locatio][outlet])
          array.push({name : JSON.parse(result._body)[locatio][outlet]});
        }
       
        var treee: Location={
          name: locatio,
          children: array
        }
        this.tree_data.push(treee);
       console.log(this.tree_data)
      }

     
      this.dataSource = new ArrayDataSource(this.tree_data);
    });
    this.initializeLineChart(0);
    this.userService.initializePieChart();
    this.userService.startOutletBroadcast();
    this.userService.initializeWebSocketConnection();
  }

  initializeLineChart(name) {
    console.log(name)
    this.userService.initializeLineChart(name);
  }
}
