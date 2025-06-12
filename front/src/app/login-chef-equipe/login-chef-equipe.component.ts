import { Component } from '@angular/core';
import { FormGroup, FormBuilder, FormControl, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { ChefEquipe } from '../Entity/ChefEquipe.Entity';
import { CrudService } from '../service/crud.service';
declare var google:any;

@Component({
  selector: 'app-login-chef-equipe',
  templateUrl: './login-chef-equipe.component.html',
  styleUrls: ['./login-chef-equipe.component.css']
})
export class LoginChefEquipeComponent {
  messageCommande = ""
  loginForm: FormGroup

  constructor(
    private fb: FormBuilder,
    private service: CrudService,
    private router: Router
  ) {
    let formControls = {
      email: new FormControl('', [
        Validators.required,
        Validators.email
      ]),
      mp: new FormControl('', [
        Validators.required,
      ])
    }

    this.loginForm = this.fb.group(formControls)
  }

  get email() { return this.loginForm.get('email') }
  get mp() { return this.loginForm.get('mp') }

  loginChef() {
    let data = this.loginForm.value;
    console.log(data);
    let chefEquipe = new ChefEquipe(
        undefined,
        undefined,
        undefined,
        undefined,
        data.email,
        undefined,
        data.mp,
        undefined,
        undefined,
        undefined,
        undefined,
        undefined,
        undefined,
        undefined
    );
   
    console.log(chefEquipe);
    if (data.email == 0 || data.mp == 0) {
        this.messageCommande = `<div class="alert alert-danger" role="alert">
            Veuillez remplir tous les champs.
        </div>`;
    } else {
        this.service.loginChefEquipe(chefEquipe).subscribe(
            res => {
                console.log(res);
                this.messageCommande = `<div class="alert alert-success" role="alert">
                    Connexion réussie.
                </div>`;
                let token = res.token;
                localStorage.setItem("myTokenChefEquipe", token);
                this.router.navigate(['accueilChef']).then(() => { window.location.reload() });
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
        )
    }
}

  ngOnInit(): void {
 
  }

  
}
