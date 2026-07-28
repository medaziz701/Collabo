import { Component } from '@angular/core';
import { FormGroup, FormBuilder, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { CrudService } from '../service/crud.service';

@Component({
  selector: 'app-login-admin',
  templateUrl: './login-admin.component.html',
  styleUrls: ['./login-admin.component.css']
})
export class LoginAdminComponent {
  messageCommande = "";
  loginForm: FormGroup;

  constructor(
    private fb: FormBuilder,
    private service: CrudService,
    private router: Router
  ) {
    this.loginForm = this.fb.group({
      email: ['', [Validators.required, Validators.email]],
      mp: ['', Validators.required]
    });
  }

  login() {
    const data = this.loginForm.value;
    
    if (!data.email || !data.mp) {
      this.messageCommande = `<div class="alert alert-danger">Veuillez remplir tous les champs</div>`;
      return;
    }

    this.service.loginAdmin(data).subscribe(
      res => {
        localStorage.setItem('myTokenAdmin', res.token);
        this.router.navigate(['homeAdmin']).then(() => window.location.reload());
      },
      err => {
        this.messageCommande = `<div class="alert alert-danger">${err.error.error || 'Erreur de connexion'}</div>`;
      }
    );
  }
}