import { BrowserModule } from "@angular/platform-browser";
import { NgModule } from "@angular/core";
import { FormsModule } from "@angular/forms";
import { AppRoutingModule } from "./app-routing.module";
import { AppComponent } from "./app.component";
import { HttpModule } from "@angular/http";
import { NgbModule } from "@ng-bootstrap/ng-bootstrap";
import { CdkTreeModule } from "@angular/cdk/tree";
import { MatButtonModule, MatIconModule } from "@angular/material";
import { LoginComponent } from "./components/login/login.component";
import { RegisterComponent } from "./components/register/register.component";
import { UserPageComponent } from "./components/user-page/user-page.component";
import { NavBarComponent } from "./components/user-page/nav-bar/nav-bar.component";
import { LeftSideMenuComponent } from "./components/user-page/left-side-menu/left-side-menu.component";
import { RightSidePanelComponent } from "./components/user-page/home/right-side-panel/right-side-panel.component";
import { HomeComponent } from "./components/user-page/home/home.component";
import { ElectricPowerMapComponent } from "./components/user-page/electric-power-map/electric-power-map.component";
import { InfoBoxComponent } from "./components/user-page/electric-power-map/info-box/info-box.component";
import { PowerSourceComponent } from "./components/user-page/electric-power-map/power-source/power-source.component";
import { BrowserAnimationsModule } from "@angular/platform-browser/animations";
import {MatSlideToggleModule} from '@angular/material/slide-toggle';
import {MatCardModule} from '@angular/material/card';
import { ElectricMapRightSidePanelComponent } from './components/user-page/electric-power-map/electric-map-right-side-panel/electric-map-right-side-panel.component';
import {MatSelectModule} from '@angular/material/select';
import {MatFormFieldModule} from '@angular/material/form-field';
import { NgxGaugeModule } from 'ngx-gauge';
import { SettingsComponent } from './components/user-page/settings/settings.component';
import {MatInputModule} from '@angular/material/input';
import { StatisticsComponent } from './components/user-page/home/right-side-panel/statistics/statistics.component';
import { RecentActivityComponent } from './components/user-page/home/right-side-panel/recent-activity/recent-activity.component';

@NgModule({
  declarations: [
    AppComponent,
    LoginComponent,
    RegisterComponent,
    UserPageComponent,
    NavBarComponent,
    LeftSideMenuComponent,
    RightSidePanelComponent,
    HomeComponent,
    ElectricPowerMapComponent,
    InfoBoxComponent,
    PowerSourceComponent,
    ElectricMapRightSidePanelComponent,
    SettingsComponent,
    StatisticsComponent,
    RecentActivityComponent
  ],
  imports: [
    BrowserModule,
    BrowserAnimationsModule,
    AppRoutingModule,
    HttpModule,
    FormsModule,
    NgbModule,
    CdkTreeModule,
    MatButtonModule,
    MatIconModule,
    MatSlideToggleModule,
    MatCardModule,
    MatSelectModule,
    MatFormFieldModule,
    NgxGaugeModule,
    MatInputModule
  ],
  providers: [],
  bootstrap: [AppComponent]
})
export class AppModule {}
