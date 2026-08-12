import { Component } from '@angular/core';
import { FormGroup, FormBuilder, FormControl, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { Client } from '../Entity/Client.Entity';
import { CrudService } from '../service/crud.service';
declare var google:any;

@Component({
  selector: 'app-login-client',
  templateUrl: './login-client.component.html',
  styleUrls: ['./login-client.component.css']
})
export class LoginClientComponent {
  messageCommande = "";
  loginForm: FormGroup;

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
        Validators.required
      ])
    };

    this.loginForm = this.fb.group(formControls);
  }

  get email() { return this.loginForm.get('email'); }
  get mp() { return this.loginForm.get('mp'); }

  loginClient() {
    let data = this.loginForm.value;
    console.log(data);
    let client = new Client(
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
    );
    
    console.log(client);
    if (data.email == 0 || data.mp == 0) {
      this.messageCommande = `<div class="alert alert-danger" role="alert">
        Veuillez remplir tous les champs.
      </div>`;
    } else {
      this.service.loginClient(client).subscribe(
        res => {
          console.log(res);
          this.messageCommande = `<div class="alert alert-success" role="alert">
            Connexion réussie.
          </div>`;
          let token = res.token;
          localStorage.setItem("myTokenClient", token);
          this.router.navigate(['accueilClient']).then(() => { window.location.reload(); });
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


  ngOnInit(): void {
    const script = document.createElement('script');
    script.src = 'https://accounts.google.com/gsi/client';
    script.async = true;
    script.defer = true;
    document.body.appendChild(script);

    script.onload = () => {

      
      google.accounts.id.initialize({
        client_id: '319989704149-gejjkcjg3n88f2uv7gnh3a1641vss4p5.apps.googleusercontent.com',
        callback: this.handleCredentialResponse.bind(this)
      });

      google.accounts.id.renderButton(
        document.getElementById('g_id_signin'),
        { theme: 'outline', size: 'medium', shape: 'pill', text: 'continue_with' }
      );

      google.accounts.id.prompt();
    };
  }

  handleCredentialResponse(response: any): void {
    const idToken = response.credential;
    console.log("ID Token:", idToken);

    this.service.signInWithGoogle(idToken).subscribe(
      res => {
        console.log('Connexion réussie via Google!', res);
        localStorage.setItem("myToken", res.token);
        this.router.navigate(['']).then(() => window.location.reload());
      },
      err => {
        console.error('Erreur de connexion Google:', err);
        this.messageCommande = `
          <div class="alert alert-danger" role="alert">
            Erreur lors de la connexion avec Google.
          </div>`;
      }
    );
  }
}
