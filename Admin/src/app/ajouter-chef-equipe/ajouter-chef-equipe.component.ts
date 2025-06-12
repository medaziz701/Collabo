import { Component } from '@angular/core';
import { FormGroup, FormBuilder, FormControl, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { ChefEquipe } from '../Entity/ChefEquipe.Entity';
import { CrudService } from '../service/crud.service';

@Component({
  selector: 'app-ajouter-chef-equipe',
  templateUrl: './ajouter-chef-equipe.component.html',
  styleUrls: ['./ajouter-chef-equipe.component.css']
})
export class AjouterChefEquipeComponent {

  messageCommande = "";
  chefEquipeForm: FormGroup;

  constructor(  private services: CrudService,  private router: Router,  private fb: FormBuilder) {
    this.chefEquipeForm = this.fb.group({
      cin: new FormControl('', [
        Validators.required,//Affichera une erreur si le champ est vide
        Validators.pattern('[0-9]{8}')//[0-9] : seulement des chiffres {8} : exactement 8 caractères
      ]),
      nom: new FormControl('', [
        Validators.required,
        Validators.minLength(2)
      ]),
      prenom: new FormControl('', [
        Validators.required
      ]),
      genre: new FormControl('', [
        Validators.required
      ]),
      dateNaissance: new FormControl('', [
        Validators.required
      ]),
      tlf: new FormControl('', [
        Validators.required,
        Validators.pattern('\\+216[0-9]{8}')
      ]),
      adresse: new FormControl('', [
        Validators.required
      ]),
      mp: new FormControl('', [
        Validators.required,
        Validators.minLength(6)
      ]),
      
      datePriseFonction: new FormControl('', [
        Validators.required
      ]),
     
      etat: new FormControl(false, [ 
        Validators.required,
    ]),
      email: new FormControl('', [
        Validators.required,
        Validators.email
      ])
    });
  }

  // Getters pour les contrôles du formulaire
  get cin() { return this.chefEquipeForm.get('cin'); }
  get nom() { return this.chefEquipeForm.get('nom'); }
  get prenom() { return this.chefEquipeForm.get('prenom'); }
  get genre() { return this.chefEquipeForm.get('genre'); }
  get dateNaissance() { return this.chefEquipeForm.get('dateNaissance'); }
  get tlf() { return this.chefEquipeForm.get('tlf'); }
  get adresse() { return this.chefEquipeForm.get('adresse'); }
  get datePriseFonction() { return this.chefEquipeForm.get('datePriseFonction'); }
  get mp() { return this.chefEquipeForm.get('mp'); }
  get email() { return this.chefEquipeForm.get('email'); }

  addNewChefEquipe() {
    let data = this.chefEquipeForm.value; // Récupère les valeurs du formulaire
    console.log(data);
    
    let chefEquipe = new ChefEquipe(
      undefined, 
      data.nom, 
      data.prenom, 
      data.tlf, 
      data.email, 
      data.adresse, 
      data.mp, 
      data.genre,
      data.cin,
      data.dateNaissance ? new Date(data.dateNaissance) : undefined, // Si dateNaissance existe → convertit en Date 
      
      data.datePriseFonction ? new Date(data.datePriseFonction) : undefined,
      
    );
    console.log(chefEquipe); // Présence de logs pour debug

    // Vérification des champs du formulaire
    if (
      data.nom == '' ||
      data.prenom == '' ||
      data.tlf == '' ||
      data.email == '' ||
      data.adresse==''||
      data.mp == '' ||
      data.genre == '' ||
      data.cin == '' ||
      data.dateNaissance==''||
      data.datePriseFonction==''
    ) {
      this.messageCommande = `<div class="alert alert-danger" role="alert">
                                Veuillez remplir tous les champs obligatoires
                              </div>`;
    } else {
      // Si validation OK Appelle le service addChefEquipe()
      this.services.addChefEquipe(chefEquipe).subscribe( // subscribe() est une méthode pour écouter les données émises par un flux asynchrone
        res => { // res contient la réponse du serveur (data renvoyée par l'API)
          console.log(res);
          this.messageCommande = `<div class="alert alert-success" role="alert">
                                    Chef d'équipe ajouté avec succès
                                  </div>`;

          this.router.navigate(['/listeChefEquipe']).then(() => { window.location.reload() });
        },
        err => {
          this.messageCommande = `<div class="alert alert-warning" role="alert">
                                  ${err.status === 409 ? 'Cet email existe déjà' : 'Une erreur est survenue'}
                                </div>`;
        }
      );

      setTimeout(() => { // Exécute la fonction après un délai (3000ms = 3s) Ici, efface le message après 3 secondes
        this.messageCommande = "";
      }, 3000);
    }
}

private markAllAsTouched() {
    Object.values(this.chefEquipeForm.controls).forEach(control => {
      control.markAsTouched(); // Marque tous les champs comme "touchés" pour afficher les erreurs
    });
}

ngOnInit(): void { 
    // Méthode appelée automatiquement après la création du composant
    // Ici vide, mais typiquement utilisé pour :
    // - Charger des données initiales
    // - Initialiser des variables
    // - Faire des appels API initiaux
}

}