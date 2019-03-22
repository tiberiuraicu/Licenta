import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { LoginComponent } from './login/login.component';
import { HttpModule } from '@angular/http';
import { RegisterComponent } from './register/register.component';
import { RouterModule } from '@angular/router';
import { UserPageComponent } from './user-page/user-page.component';
import { NavBarComponent } from './user-page/nav-bar/nav-bar.component';
import { LeftSideMenuComponent } from './user-page/left-side-menu/left-side-menu.component';
import { RightSidePanelComponent } from './user-page/right-side-panel/right-side-panel.component';
import {NgbModule} from '@ng-bootstrap/ng-bootstrap';
import { CdkTreeModule } from '@angular/cdk/tree';
import {MatButtonModule, MatIconModule} from '@angular/material';

@NgModule({
  declarations: [
    AppComponent,
    LoginComponent,
    RegisterComponent,
    UserPageComponent,
    NavBarComponent,
    LeftSideMenuComponent,
    RightSidePanelComponent,


  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    HttpModule,
    FormsModule,
    NgbModule,
    CdkTreeModule,
    MatButtonModule,
    MatIconModule,
    RouterModule.forRoot([
     
      { path: "login", component: LoginComponent },
      { path: "register", component: RegisterComponent },
      { path: "user", component: UserPageComponent },
    
    ])
  ],
  providers: [],
  bootstrap: [AppComponent]
})
export class AppModule { }
