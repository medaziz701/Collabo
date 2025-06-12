import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { HomeComponent } from './home/home.component';
import { ContactComponent } from './contact/contact.component';
import { registreComponent } from './registre/registre.component';
import { LoginComponent } from './login/login.component';
import { FormsModule, ReactiveFormsModule } from '@angular/forms'; 
import { HttpClientModule } from '@angular/common/http';
import { HeaderComponent } from './header/header.component';
import { ChoixComponent } from './choix/choix.component';
import { LoginChefEquipeComponent } from './login-chef-equipe/login-chef-equipe.component';
import { LoginClientComponent } from './login-client/login-client.component';
import { AjouterProjetComponent } from './ajouter-projet/ajouter-projet.component';
import { VideoCallComponent } from './video-call/video-call.component';
import { ListeProjetComponent } from './liste-projet/liste-projet.component';
import { RegistreClientComponent } from './registre-client/registre-client.component';
import { MesProjetsComponent } from './mes-projets/mes-projets.component';
import { ListeProjetClientComponent } from './liste-projet-client/liste-projet-client.component';
import { CodeEditorComponent } from './code-editor/code-editor.component';
import { ProjectChatComponent } from './project-chat/project-chat.component';
import { PlanningComponent } from './planning/planning.component';
import { CalendarModule, DateAdapter } from 'angular-calendar';
import { adapterFactory } from 'angular-calendar/date-adapters/date-fns';
import { EditChefEquipeProfileComponent } from './edit-chef-equipe-profile/edit-chef-equipe-profile.component';
import { ProfilComponentTsComponent } from './profil.component.ts/profil.component.ts.component';
import { AboutUsComponent } from './about-us/about-us.component';
import { FooterComponent } from './footer/footer.component';
import { PlanningViewComponent } from './planning-view/planning-view.component';
import { AccueilChefEquipeComponent } from './accueil-chef-equipe/accueil-chef-equipe.component';
import { AccueilClientComponent } from './accueil-client/accueil-client.component';
import { AccueilDeveloppeurComponent } from './accueil-developpeur/accueil-developpeur.component';
import { CommentSectionComponent } from './comment-section/comment-section.component';




@NgModule({
  declarations: [
    AppComponent,
    
    HomeComponent,
    ContactComponent,
    registreComponent,
    LoginComponent,
    HeaderComponent,
    ChoixComponent,
    LoginChefEquipeComponent,
    LoginClientComponent,
    AjouterProjetComponent,
    VideoCallComponent,
    ListeProjetComponent,
    RegistreClientComponent,
    MesProjetsComponent,
    ListeProjetClientComponent,
    CodeEditorComponent,
    ProjectChatComponent,
    PlanningComponent,
    EditChefEquipeProfileComponent,
    ProfilComponentTsComponent,
    AboutUsComponent,
    FooterComponent,
    PlanningViewComponent,
    AccueilChefEquipeComponent,
    AccueilClientComponent,
    AccueilDeveloppeurComponent,
    CommentSectionComponent,

    

    
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    ReactiveFormsModule,
    FormsModule,
    HttpClientModule,


    CalendarModule.forRoot({
      provide: DateAdapter,
      useFactory: adapterFactory
    })


  ],
  providers: [],
  bootstrap: [AppComponent]
})
export class AppModule { }
