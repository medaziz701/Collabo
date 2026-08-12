import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { HomeComponent } from './home/home.component';
import { ContactComponent } from './contact/contact.component';
import { LoginComponent } from './login/login.component';
import { registreComponent } from './registre/registre.component';
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
import { EditChefEquipeProfileComponent } from './edit-chef-equipe-profile/edit-chef-equipe-profile.component';
import { ProfilComponentTsComponent } from './profil.component.ts/profil.component.ts.component';
import { AboutUsComponent } from './about-us/about-us.component';
import { PlanningViewComponent } from './planning-view/planning-view.component';
import { AccueilChefEquipeComponent } from './accueil-chef-equipe/accueil-chef-equipe.component';
import { AccueilClientComponent } from './accueil-client/accueil-client.component';
import { AccueilDeveloppeurComponent } from './accueil-developpeur/accueil-developpeur.component';

const routes: Routes = [
  {path:'',component:HomeComponent},
  { path: 'contact', component: ContactComponent },
  { path: 'aboutUS', component: AboutUsComponent },

  { path: 'logindeveloppeur', component: LoginComponent },
  { path: 'loginChefEquipe', component: LoginChefEquipeComponent },
  { path: 'inscritClient', component: RegistreClientComponent },

  { path: 'tache/:id/code', component: CodeEditorComponent },
    { path: 'vueRendezVous', component: PlanningViewComponent },


    { path: 'accueilChef', component: AccueilChefEquipeComponent },
        { path: 'accueilClient', component: AccueilClientComponent },
                { path: 'accueildeveloppeur', component: AccueilDeveloppeurComponent },


    






  { path: 'loginClient', component: LoginClientComponent },
  { path: 'inscritdeveloppeur', component: registreComponent },
  { path: 'AjouterContact', component:ContactComponent },
  { path: 'AjouterProjet', component:AjouterProjetComponent },
  { path: 'chatvideo', component:VideoCallComponent },
  { path: 'listeProjets', component:ListeProjetComponent },
  { path: 'MesProjet', component:MesProjetsComponent },
  { path: 'listeProjClient', component:ListeProjetClientComponent },
  { path: 'projetchat', component:ProjectChatComponent },
  { path: 'Rendezvous', component:PlanningComponent },

  { path: 'editProfil', component:EditChefEquipeProfileComponent },
  { path: 'profil', component:ProfilComponentTsComponent },











  { path: 'choix', component: ChoixComponent },


  
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }


