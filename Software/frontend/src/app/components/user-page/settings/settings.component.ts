import { Component} from "@angular/core";
import { Http,Headers } from "@angular/http";

@Component({
  selector: "app-settings",
  templateUrl: "./settings.component.html",
  styleUrls: ["./settings.component.scss"]
})
export class SettingsComponent {


  selectedProfilePicture: File;

  constructor(private http: Http) {}

  model = {
    userId: localStorage.getItem("userId"),
    firstName: "",
    lastName: "",
    email: "",
    password: "",
    phoneNumber: "",
    country: "",
    locality: "",
    street: ""
  };


  onSubmit() {
    this.updateUserProfilePicture();
    this.updateUserData();
  }

  onFileInput(event) {
    this.selectedProfilePicture = <File>event.target.files[0];
  }

  updateUserProfilePicture() {
    var userAuthentificationHeader = new Headers({
      Authorization: "Bearer " + localStorage.getItem("token"),
      userId:localStorage.getItem("currentId")
    });
    if (
      this.selectedProfilePicture.type == "image/jpeg" ||
      this.selectedProfilePicture.type == "image/png"
    ) {
      const formData = new FormData();
      formData.append(
        "file",
        this.selectedProfilePicture,
        this.selectedProfilePicture.name
      );
      this.http
        .post("http://localhost:8080/user/updateUserProfilePicture", formData, {
          headers: userAuthentificationHeader
        })
        .subscribe(response => {
          console.log(response);
        });
    }
  }
  updateUserData() {
    var userAuthentificationHeader = new Headers({
      Authorization: "Bearer " + localStorage.getItem("token"),
      userId:localStorage.getItem("currentId")
    });
      this.http
        .post("http://localhost:8080/user/updateUserData", this.model, {
          headers: userAuthentificationHeader
        })
        .subscribe(response => {
          console.log(response);
        });
    }
  }

