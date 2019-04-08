import { Component, OnInit } from '@angular/core';
import { NestedTreeControl } from "@angular/cdk/tree";
import { ArrayDataSource } from "@angular/cdk/collections"
import { UserService } from 'src/app/services/home-service/home.service';

interface Location {
  name: string;
  children?: Outlet[];
}
interface Outlet {
  name: string;
}

@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.scss']
})
export class HomeComponent implements OnInit {

  constructor(private userService: UserService) {
    
  }

  treeControl = new NestedTreeControl<Location>(node => node.children);
  tree_data = [];
  dataSource;

  hasChild = (_: number, node: Location) =>
    !!node.children && node.children.length > 0;

  ngOnInit() {
    
    this.userService.getAllOutlets().subscribe(result => {
      //got response as string
      let resultAsJson = JSON.parse(result._body);
      console.log(resultAsJson)
      for (var location in resultAsJson) {
        console.log(location
          )
        var outletsInLocation: Outlet[] = [];

        for (var outlet in resultAsJson[location]) {
          outletsInLocation.push({ name: resultAsJson[location][outlet] });
        }

        var locationForTree: Location = {
          name: location,
          children: outletsInLocation
        };
        this.tree_data.push(locationForTree);
      }
      this.dataSource = new ArrayDataSource(this.tree_data);
    });
    this.userService.initializeWebSocketConnection();
    this.initializeLineChart(0);
    this.userService.initializePieChart();
    this.userService.startOutletBroadcast();
 
  }

  initializeLineChart(name) {
    this.userService.initializeLineChart(name);
  }

 
}
