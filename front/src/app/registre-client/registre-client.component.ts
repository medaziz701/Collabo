import { Component } from '@angular/core';
import { FormGroup, FormBuilder, FormControl, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { Client } from '../Entity/Client.Entity';
import { CrudService } from '../service/crud.service';

@Component({
  selector: 'app-registre-client',
  templateUrl: './registre-client.component.html',
  styleUrls: ['./registre-client.component.css']
})
export class RegistreClientComponent {

  messageCommande = "";
  clientForm: FormGroup;
  newClient = new Client();

  constructor(
    private services: CrudService, 
    private router: Router, 
    private fb: FormBuilder
  ) {
    let formControls = {
      nom: new FormControl('', [
        Validators.required,
        Validators.minLength(2)
      ]),
      prenom: new FormControl('', [
        Validators.required
      ]),
      tlf: new FormControl('', [
        Validators.required,
        Validators.pattern('[0-9]{8}')
      ]),
      email: new FormControl('', [
        Validators.required,
        Validators.email
      ]),
      cin: new FormControl('', [
        Validators.required,
        Validators.pattern('[0-9]{8}') 
      ]),
      mp: new FormControl('', [
        Validators.required,
        Validators.minLength(6)
      ]),
      adresse: new FormControl(''), // Pas de validators pour l'adresse
      genre: new FormControl('', [
        Validators.required
      ]),
      etat: new FormControl(false, [ 
        Validators.required,
    ])
    }
    this.clientForm = this.fb.group(formControls);
  }

  // Getters pour les contrôles du formulaire
 
     
  get nom() { return this.clientForm.get('nom'); }
  get prenom() { return this.clientForm.get('prenom'); }
  get tlf() { return this.clientForm.get('tlf'); }
  get email() { return this.clientForm.get('email'); }
  get cin() { return this.clientForm.get('cin'); }
  get mp() { return this.clientForm.get('mp'); }
  get adresse() { return this.clientForm.get('adresse'); }
  get genre() { return this.clientForm.get('genre'); }
 

  addNewClient() {
    // Marquer tous les champs comme touchés
    this.markFormGroupTouched(this.clientForm);
  
    let data = this.clientForm.value;
    console.log(data);
    
    if (this.clientForm.invalid) {
      this.messageCommande = `<div class="alert alert-danger" role="alert">
                              Veuillez remplir tous les champs obligatoires correctement
                            </div>`;
      return;
    }
  
    let client = new Client(
      undefined, 
      data.nom, 
      data.prenom, 
      data.tlf, 
      data.email, 
      data.cin,
      data.mp, 
      data.adresse, 
      data.genre
    );
  
    this.services.addClient(client).subscribe(
      res => {
        console.log(res);
        this.messageCommande = `<div class="alert alert-success" role="alert">
                                Client créé avec succès
                              </div>`;
        this.router.navigate(['/listeClient']);
      },
      err => {
        console.error(err);
        this.messageCommande = `<div class="alert alert-danger" role="alert">
                                ${err.error?.message || 'Une erreur est survenue'}
                              </div>`;
      }
    );
  
    setTimeout(() => {
      this.messageCommande = "";
    }, 5000);
  }
  
  // Ajoutez cette méthode pour marquer tous les champs comme touchés
  private markFormGroupTouched(formGroup: FormGroup) {
    Object.values(formGroup.controls).forEach(control => {
      control.markAsTouched();
  
      if (control instanceof FormGroup) {
        this.markFormGroupTouched(control);
      }
    });
  }

  ngOnInit(): void {
  }
}
