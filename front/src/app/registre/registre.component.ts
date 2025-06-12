import { Component } from '@angular/core';
import { FormGroup, FormBuilder, FormControl, Validators, AbstractControl } from '@angular/forms';
import { Router } from '@angular/router';
import { Developpeur } from '../Entity/Developpeur.Entity';
import { CrudService } from '../service/crud.service';


@Component({
  selector: 'app-ajouter-developpeur',
  templateUrl: './registre.component.html', 
  styleUrls: ['./registre.component.css']
})
export class registreComponent {

  messageCommande = "";
 developpeurForm: FormGroup;
  userFile: any;
  public imagePath: any;
  emailURL: any;
  newProduit = new Developpeur();
  public message!: string;

  constructor(private services: CrudService, private router: Router, private fb: FormBuilder) {
    let formControls = {
      nom: new FormControl('', [
        Validators.required,
        Validators.minLength(4)
    ]),
    prenom: new FormControl('', [
        Validators.required,
    ]),
    tlf: new FormControl('', [
        Validators.required,
    ]),
    cin: new FormControl('', [
      Validators.required,
      Validators.pattern('[0-9]{8}')
    ]),
    email: new FormControl('', [
        Validators.required,
        Validators.email
    ]),
   
    mp: new FormControl('', [
        Validators.required,
        Validators.minLength(4)
    ]),
    adresse: new FormControl('', [
        Validators.required,
    ]),
    specialite: new FormControl('', [
        Validators.required,
    ]),
    genre: new FormControl('', [
        Validators.required,
    ]),
    etat: new FormControl(false, [ 
      Validators.required,
  ])
      

    }
    this.developpeurForm = this.fb.group(formControls);
  }

  get nom() { return this.developpeurForm.get('nom'); }
  get prenom() { return this.developpeurForm.get('prenom'); }
  get tlf() { return this.developpeurForm.get('tlf'); }
  get cin() { return this.developpeurForm.get('cin'); }
  get email() { return this.developpeurForm.get('email'); }
  get mp() { return this.developpeurForm.get('mp'); }
  get adresse() { return this.developpeurForm.get('adresse'); }
  get specialite() { return this.developpeurForm.get('specialite'); }
  get genre() { return this.developpeurForm.get('genre'); }

  
 

  
  

  addNewDeveloppeur() {
    let data = this.developpeurForm.value;
    console.log(data);
    let developpeur = new Developpeur(
      undefined, data.nom, data.prenom,data.tlf, data.cin,data.email,  data.mp, data.adresse,data.specialite,false, true,data.genre
    );
    console.log(developpeur);

    // Vérification des champs du formulaire
    if (
      data.nom == '' ||
      data.prenom == '' ||
      data.tlf == '' ||
      data.cin==''||
      data.email == '' ||
      data.mp == '' ||
      data.adresse == '' ||
      data.specialite == '' ||
      data.genre == '' 
      
     ) {
      this.messageCommande = `<div class="alert alert-danger" role="alert">
                                Veuillez remplir tous les champs correctement
                              </div>`;
    } else {
      this.services.addDeveloppeur(developpeur).subscribe(
        res => {
          console.log(res);
          this.messageCommande = `<div class="alert alert-success" role="alert">
                                    Ajout effectué avec succès
                                  </div>`;

          this.router.navigate(['/listeDeveloppeur']).then(() => { window.location.reload() });
        },
        err => {
          this.messageCommande = `<div class="alert alert-warning" role="alert">
                                    L'email existe déjà !!!
                                  </div>`;
        }
      );

      setTimeout(() => {
        this.messageCommande = "";
      }, 3000);
    }
  }

  ngOnInit(): void {
  }
}