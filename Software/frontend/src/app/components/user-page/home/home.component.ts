import { Component, OnInit, OnDestroy } from "@angular/core";
import { NestedTreeControl } from "@angular/cdk/tree";
import { ArrayDataSource } from "@angular/cdk/collections";
import { UserService } from "src/app/services/home-service/home.service";

interface Location {
  name: string;
  children?: Outlet[];
}
interface Outlet {
  name: string;
}

@Component({
  selector: "app-home",
  templateUrl: "./home.component.html",
  styleUrls: ["./home.component.scss"]
})
export class HomeComponent implements OnInit, OnDestroy {
  constructor(private userService: UserService) {}

  ngOnDestroy(): void {
    clearInterval(this.userService.pieChartRequestInterval);
    clearInterval(this.userService.lineChartRequestInterval);
  }
  userInfo = {
    profilePictureURL:
      "./../../../../assets/resources/profile-picture-default.png",
    firstName: "first",
    lastName: "last",
    location: "location",
    phoneNumber:"phoneNumber",
    email:"email"
  };

  treeControl = new NestedTreeControl<Location>(node => node.children);
  tree_data = [];
  dataSource;

  hasChild = (_: number, node: Location) =>
    !!node.children && node.children.length > 0;

  ngOnInit() {
  
    this.userService.getProfilePicture().subscribe(res => {
      this.userInfo.profilePictureURL = JSON.parse(JSON.stringify(res))._body;
    });

    this.userService.getUserDetails().subscribe(resp=>{
     this.userInfo.firstName=JSON.parse(resp._body)["firstName"];
     this.userInfo.lastName=JSON.parse(resp._body)["lastName"];
     this.userInfo.location=JSON.parse(resp._body)["location"];
     this.userInfo.phoneNumber=JSON.parse(resp._body)["phoneNumber"];
     this.userInfo.email=JSON.parse(resp._body)["email"];
    });
    this.userService.getAllOutlets().subscribe(result => {
      //got response as string
      let resultAsJson = JSON.parse(result._body);
      console.log(resultAsJson);
      for (var location in resultAsJson) {
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

    this.initializeLineChart(0);
    this.userService.getOutletPowerConsumedForLineChart();
    this.userService.initializePieChart();
  }

  initializeLineChart(name) {
    this.userService.initializeLineChart(name);
  }
}
