import { Component } from '@angular/core';
import { FormGroup, FormControl, FormBuilder, Validators, FormArray } from '@angular/forms';
import { CrudService } from '../service/crud.service';
import { Router } from '@angular/router';
import { Client } from '../Entity/Client.Entity';
import { Developpeur } from '../Entity/Developpeur.Entity';

@Component({
  selector: 'app-ajouter-projet',
  templateUrl: './ajouter-projet.component.html',
  styleUrls: ['./ajouter-projet.component.css']
})
export class AjouterProjetComponent {
  messageCommande = "";
  AjouterForm: FormGroup;
  userFile: any;//Variables pour la gestion des fichiers 
  message = "";
  imagePath: any;
  imgURL: any;
  listClient: Client[] ;
  listDeveloppeurs: Developpeur[] = [];
  developpeursSelectionnes: number[] = [];//Tableaux pour gérer les sélections 
  tachesDeveloppeurs: { 
    [key: number]: { 
        description: string, 
        dateLimite: string 
    } 
} = {};

  constructor(
    private services: CrudService,
    private router: Router,
    private fb: FormBuilder//FormBuilder: Pour créer le formulaire
    
  ) {
    this.AjouterForm = this.fb.group({
      nom: ['', Validators.required],
      client: ['', Validators.required],
      datedeb: ['', Validators.required],
      datefin: [''],
      description: ['', Validators.required],
      statut: ['EN_COURS', Validators.required],
      nomEquipe: ['', Validators.required],
      descriptionEquipe: ['', Validators.required],
      domaineSpecialisation: ['', Validators.required],
      
    });
  }
  updateTacheForDeveloppeur(devId: number, event: any, field: 'description' | 'dateLimite') {
    if (!this.tachesDeveloppeurs[devId]) {
        this.tachesDeveloppeurs[devId] = { description: '', dateLimite: '' };
    }
    this.tachesDeveloppeurs[devId][field] = event.target.value;
}

  toggleDeveloppeurSelection(id: number) {
    const index = this.developpeursSelectionnes.indexOf(id);
    if (index === -1) {
      this.developpeursSelectionnes.push(id);
    } else {
      this.developpeursSelectionnes.splice(index, 1);
    }
  }
  

  isDeveloppeurSelected(id: number): boolean {
    return this.developpeursSelectionnes.includes(id);
  }

  onSelectFile(event: any) {//Vérifie et charge l'image sélectionnée
    if (event.target.files.length > 0) {// Vérifie si un fichier a été sélectionné
      const file = event.target.files[0];//Récupère le premier fichier sélectionné
      this.userFile = file;//Stocke le fichier dans une variable du composant
      var mimeType = event.target.files[0].type;//Récupère le type MIME du fichier (ex: "image/jpeg")
      if (mimeType.match(/image\/*/) == null) {//Vérification que c'est bien une image avec une expression régulière
        this.message = 'Seules les images sont supportées.';
        return;
      }
      var reader = new FileReader();//Crée un FileReader pour lire le contenu du fichier
      this.imagePath = file;
      reader.readAsDataURL(file);//Convertit le fichier en URL de données (base64)
      reader.onload = (_event) => {
        this.imgURL = reader.result;//Quand la lecture est terminée (onload), stocke le résultat dans imgURL pour prévisualisation
      };
    }
  }

  get nom() { return this.AjouterForm.get('nom'); }
  get client() { return this.AjouterForm.get('client'); }
  get datedeb() { return this.AjouterForm.get('datedeb'); }
  get datefin() { return this.AjouterForm.get('datefin'); }
  get description() { return this.AjouterForm.get('description'); }
  get statut() { return this.AjouterForm.get('statut'); }
  get img() { return this.AjouterForm.get('img'); }
  get nomEquipe() { return this.AjouterForm.get('nomEquipe'); }
  get descriptionEquipe() { return this.AjouterForm.get('descriptionEquipe'); }
  get domaineSpecialisation() { return this.AjouterForm.get('domaineSpecialisation'); }

  cancel() {
    this.router.navigate(['/listprojet']);
  }

 

  addNewProjet() {
    if (this.AjouterForm.invalid) {
        this.messageCommande = `<div class="alert alert-danger">Veuillez remplir tous les champs obligatoires</div>`;
        return;
    }

    if (this.developpeursSelectionnes.length === 0) {
        this.messageCommande = `<div class="alert alert-danger">Veuillez sélectionner au moins un développeur</div>`;
        return;
    }

    const chefEquipeId = this.services.getChefEquipeInfo()?.id;
    const data = this.AjouterForm.value;

    // Préparation des tâches dans le nouveau format
    const tachesDeveloppeurs: {[key: number]: {description: string, dateLimite: string}} = {};
    for (const devId in this.tachesDeveloppeurs) {
        tachesDeveloppeurs[devId] = {
            description: this.tachesDeveloppeurs[devId].description,
            dateLimite: this.tachesDeveloppeurs[devId].dateLimite
        };
    }

    const projetData = {
        nom: data.nom,
        datedeb: new Date(data.datedeb).toISOString().split('T')[0],//Transforme la date en une chaîne ISO comme 2024-07-18T00:00:00.000Z 
        datefin: data.datefin ? new Date(data.datefin).toISOString().split('T')[0] : null,//  .split('T')[0]Coupe la chaîne en deux parties autour du T
        description: data.description,
        statut: 'EN_COURS',
        img: this.imgURL,
        idClient: Number(data.client),
        idChefEquipe: chefEquipeId,
        developpeursIds: this.developpeursSelectionnes,
        nomEquipe: data.nomEquipe,
        descriptionEquipe: data.descriptionEquipe,
        domaineSpecialisation: data.domaineSpecialisation,
        tachesDeveloppeurs: tachesDeveloppeurs  
    };

    this.services.addProjet(projetData).subscribe({
      next: (res: any) => {
        if (res.id) {
          // Mettre à jour la disponibilité des développeurs avant de les ajouter
          this.developpeursSelectionnes.forEach(devId => {
            this.services.updateDisponibilite(devId, false).subscribe(() => {
              // Après mise à jour de la disponibilité, ajouter à l'équipe
              this.services.ajouterMembreAEquipe(res.id, devId).subscribe({
                next: () => {
                  // Rafraîchir la liste après chaque ajout
                  this.services.getDeveloppeursDisponibles().subscribe(devs => {
                    this.listDeveloppeurs = devs;
                  });
                },
                error: (err) => console.error(err)
              });
            });
          });
  
          this.messageCommande = `<div class="alert alert-success">Projet et équipe créés avec succès</div>`;
         
          setTimeout(() => {
            this.router.navigate(['/listprojet']);
          }, 2000);
        }
        
      },
      error: (err) => {
        this.messageCommande = `<div class="alert alert-danger">Échec de la création du projet</div>`;
        console.error("Erreur création projet :", err);
      }
    });
}
// Méthode pour ajouter un membre à une équipe
ajouterMembreAEquipe(equipeId: number, developpeurId: number): void {
  this.services.ajouterMembreAEquipe(equipeId, developpeurId).subscribe({
    next: () => {//next gère chaque valeur émise ,complete quand le flux se termine, error  gère les erreurs
      console.log('Membre ajouté et disponibilité mise à jour');
      // Rafraîchir la liste des développeurs disponibles
      this.services.getDeveloppeursDisponibles().subscribe(devs => {
        this.listDeveloppeurs = devs;
      });
    },
    error: (err) => console.error('Erreur:', err)
  });
}

// Méthode pour retirer un membre d'une équipe
retirerMembreDeEquipe(equipeId: number, developpeurId: number): void {
  this.services.retirerMembreDeEquipe(equipeId, developpeurId).subscribe({
    next: () => {
      console.log('Membre retiré et disponibilité mise à jour');
      // Rafraîchir la liste des développeurs disponibles
      this.services.getDeveloppeursDisponibles().subscribe(devs => {
        this.listDeveloppeurs = devs;
      });
    },
    error: (err) => console.error('Erreur:', err)
  });
}
  ngOnInit(): void {
    this.services.getClient().subscribe(client => {
      this.listClient = client;
      console.log(client)
    });

    this.services.getDeveloppeursDisponibles().subscribe(devs => {
      this.listDeveloppeurs = devs;
    });
  }
}
