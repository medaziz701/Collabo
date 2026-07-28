import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { HomeComponent } from './home/home.component';

import { ListeDeveloopeurComponent } from './liste-developpeur/liste-developpeur.component';
import { AjouterChefEquipeComponent } from './ajouter-chef-equipe/ajouter-chef-equipe.component';
import { ListeClientComponent } from './liste-client/liste-client.component';
import { ListeChefequipeComponent } from './liste-chefequipe/liste-chefequipe.component';
import { ListeProjetsComponent } from './liste-projets/liste-projets.component';
import { ListeContactComponent } from './liste-contact/liste-contact.component';
import { LoginAdminComponent } from './login-admin/login-admin.component';


const routes: Routes = [
  {path:'ajouterChef',component:AjouterChefEquipeComponent},


  {path:'homeAdmin',component:HomeComponent},
  {path:'listeDeveloppeur',component:ListeDeveloopeurComponent},
  {path:'listeClient',component:ListeClientComponent},
  {path:'listeChefEquipe',component:ListeChefequipeComponent},
  {path:'listeProjet',component:ListeProjetsComponent},
  {path:'listeContact',component:ListeContactComponent},
    {path:'',component:LoginAdminComponent},










];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
