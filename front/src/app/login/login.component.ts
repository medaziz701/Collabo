import { Component } from '@angular/core';
import { FormGroup, FormBuilder, FormControl, Validators } from '@angular/forms';//Classes pour gérer les formulaires réactifs
import { Router } from '@angular/router';
import { Developpeur } from '../Entity/Developpeur.Entity'; 
import { CrudService } from '../service/crud.service';

@Component({//Définit les sélecteurs
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent {
  messageCommande = ""// Pour afficher des messages à l'utilisateur
  loginForm: FormGroup// Le formulaire de connexion
    constructor(
      private fb: FormBuilder,
      private service:CrudService,
      private router:Router
    ) { 
      let formControls = {
        email: new FormControl('',[
          Validators.required,
          Validators.email
          
        ]),
        mp: new FormControl('',[
          Validators.required,
         
        ])
      }//Initialise le formulaire avec 2 champs:
  
      this.loginForm = this.fb.group(formControls)
    }
  
    get email() { return this.loginForm.get('email') }
    get mp() { return this.loginForm.get('mp') }
    login() {
      let data = this.loginForm.value;
      console.log(data);
    
      let developpeur = new Developpeur(
        undefined, 
        undefined, 
        undefined, 
        undefined, 
        undefined, 
        data.email,
        data.mp,   
        undefined,
        undefined,  
        undefined,  
        undefined  
      );
    
      if (!data.email || !data.mp) {
        this.messageCommande = `<div class="alert alert-danger" role="alert">
          Veuillez remplir tous les champs.
        </div>`;
      } else {
        this.service.loginDeveloppeur(developpeur).subscribe(
          res => {
            console.log(res);
            // Stockage du token
            localStorage.setItem('myTokenDeveloppeur', res.token);
            
            this.messageCommande = `<div class="alert alert-success" role="alert">
              Connexion réussie.
            </div>`;
            
            // Redirection simple sans rechargement this.router.navigate(['accueilClient']).then(() => { window.location.reload(); });
          
            this.router.navigate(['accueildeveloppeur']).then(() => { window.location.reload(); });
          },
          err => {
            console.log(err);
            if (err.status === 401) {
              if (err.error.message === "Votre compte n'est pas encore activé par l'administrateur") {
                this.messageCommande = `<div class="alert alert-danger" role="alert">
                  Votre compte n'est pas encore activé par l'administrateur.
                </div>`;
              } else if (err.error.message === "Email not found") {
                this.messageCommande = `<div class="alert alert-danger" role="alert">
                  Email invalide. Aucun compte trouvé avec cette adresse email.
                </div>`;
              }  
            } else {
              this.messageCommande = `<div class="alert alert-danger" role="alert">
                Email ou mot de passe incorrect.
              </div>`;
            }
          }
        );
      }
    }
}
