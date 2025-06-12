import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { HomeComponent } from './home/home.component';
import { HeaderComponent } from './header/header.component';
import { MenuComponent } from './menu/menu.component';
import { FooterComponent } from './footer/footer.component';
import{HttpClientModule} from '@angular/common/http';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { ListeDeveloopeurComponent } from './liste-developpeur/liste-developpeur.component';
import { ListeClientComponent } from './liste-client/liste-client.component';
import { ListeChefequipeComponent } from './liste-chefequipe/liste-chefequipe.component';
import { AjouterChefEquipeComponent } from './ajouter-chef-equipe/ajouter-chef-equipe.component';
import { ListeProjetsComponent } from './liste-projets/liste-projets.component';
import { ListeContactComponent } from './liste-contact/liste-contact.component';
import { LoginAdminComponent } from './login-admin/login-admin.component';

@NgModule({
  declarations: [
    AppComponent,
    
    HomeComponent,
    HeaderComponent,
    MenuComponent,
    FooterComponent,
    ListeDeveloopeurComponent,
    ListeClientComponent,
    ListeChefequipeComponent,
    AjouterChefEquipeComponent,
    ListeProjetsComponent,
    ListeContactComponent,
    LoginAdminComponent,
  
    
    
  ],
  imports: [
    BrowserModule,
    FormsModule,
    ReactiveFormsModule,
    HttpClientModule,//hedhi t3aref les methodes .get .post etc bch njmo norbto angular b spring boot

    AppRoutingModule
  ],
  providers: [],
  bootstrap: [AppComponent]
})
export class AppModule { }
