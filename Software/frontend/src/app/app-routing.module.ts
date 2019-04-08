import { NgModule } from "@angular/core";
import { Routes, RouterModule } from "@angular/router";
import { LoginComponent } from './components/login/login.component';
import { RegisterComponent } from './components/register/register.component';
import { UserPageComponent } from './components/user-page/user-page.component';
import { HomeComponent } from './components/user-page/home/home.component';
import { ElectricPowerMapComponent } from './components/user-page/electric-power-map/electric-power-map.component';
import { SettingsComponent } from './components/user-page/settings/settings.component';

const routes: Routes = [
  { path: "login", component: LoginComponent },
  { path: "register", component: RegisterComponent },
  {
    path: "user",
    component: UserPageComponent,
    children: [
      { path: "", redirectTo: "", pathMatch: "full" },
      { path: "home", component: HomeComponent },
      { path: "electricMap", component: ElectricPowerMapComponent},
      { path: "settings", component: SettingsComponent}
    ]
  }
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule {}
