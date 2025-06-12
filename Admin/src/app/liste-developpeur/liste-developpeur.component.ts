import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { CrudService } from '../service/crud.service';
import { Developpeur } from '../Entity/Developpeur';
import Swal from 'sweetalert2';

@Component({
  selector: 'app-liste-develoopeur',
  templateUrl: './liste-developpeur.component.html',
  styleUrls: ['./liste-developpeur.component.css']
})
export class ListeDeveloopeurComponent {
 listeDeveloppeur:Developpeur[]

  constructor(private servive:CrudService,private router:Router){}
  ngOnInit():void{
    this.servive.getDeveloppeur().subscribe(developpeur=>{
      this.listeDeveloppeur=developpeur})
  }
  DeleteDeveloppeur(developpeur: Developpeur){
    if(confirm("Voulez vous supprimer ce message de develoopeur avec l'ID " +developpeur.id+ "?")) {
       this.servive.onDeleteDeveloppeur(developpeur.id).subscribe(() => {
        this.router.navigate(['/listedeveloppeur']).then(() => {window.location.reload()})})}
}

updateDeveloppeurEtat(developpeur: Developpeur) {
  let updatedDev = {
    id: developpeur.id,
    nom: developpeur.nom,
    prenom: developpeur.prenom,
    tlf: developpeur.tlf,
    email: developpeur.email,
    adresse: developpeur.adresse,
    specialite: developpeur.specialite,
    genre: developpeur.genre,
    etat: !developpeur.etat // Inverse l'état actuel
   
  };

  this.servive.updateDeveloppeur(updatedDev, developpeur.id).subscribe(
    res => {
      // Mettre à jour localement
      developpeur.etat = !developpeur.etat;
      Swal.fire({
        icon: developpeur.etat ? "success" : "error",
        title: developpeur.etat ? "Compte activé" : "Compte désactivé",
        text: `Le compte a été ${developpeur.etat ? 'activé' : 'désactivé'} avec succès.`,
        timer: 3000
      });
    },
    err => {
      console.error(err);
      Swal.fire("Erreur", "Une erreur est survenue", "error");
    }
  );
}


}




