import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { ChefEquipe } from '../Entity/ChefEquipe.Entity';
import { CrudService } from '../service/crud.service';
import Swal from 'sweetalert2';


@Component({
  selector: 'app-liste-chefEquipe',
  templateUrl: './liste-chefEquipe.component.html',
  styleUrls: ['./liste-chefEquipe.component.css']
})
export class ListeChefequipeComponent {
listeChefEquipe: ChefEquipe[];

  constructor(private servive: CrudService, private router: Router) {}

  ngOnInit(): void {
    this.servive.getChefEquipe().subscribe(chefEquipe => {
      this.listeChefEquipe = chefEquipe;
    });
  }



  DeleteChefEquipe(chefEquipe: ChefEquipe) {
    if(confirm("Voulez-vous supprimer ce message de chef d'équipe avec l'ID " + chefEquipe.id + " ?")) {
      this.servive.onDeleteChefEquipe(chefEquipe.id).subscribe(() => {
        this.router.navigate(['/listeChefEquipe']).then(() => {
          window.location.reload(); 
        });
      });
    }
  }

updateChefEquipeEtat(chefEquipe: ChefEquipe) {
  const updatedChef = {
    id: chefEquipe.id,
    nom: chefEquipe.nom,
    prenom: chefEquipe.prenom,
    tlf: chefEquipe.tlf,
    email: chefEquipe.email,
    adresse: chefEquipe.adresse,
    genre: chefEquipe.genre,
    cin: chefEquipe.cin,
    dateNaissance: chefEquipe.dateNaissance,
    datePriseFonction: chefEquipe.datePriseFonction,
    etat: !chefEquipe.etat // Inverse simplement l'état actuel
  };

  this.servive.updateChefEquipe(updatedChef, chefEquipe.id).subscribe({
    next: (res) => {
      // Mise à jour locale de l'état
      chefEquipe.etat = !chefEquipe.etat;
      
      // Notification unifiée
      Swal.fire({
        icon: chefEquipe.etat ? "success" : "error",
        title: chefEquipe.etat ? "Compte activé" : "Compte désactivé",
        text: `Le compte chef d'équipe a été ${chefEquipe.etat ? 'activé' : 'désactivé'} avec succès.`,
        timer: 3000,
        showConfirmButton: false
      });
    },
    error: (err) => {
      console.error("Erreur lors de la mise à jour:", err);
      Swal.fire({
        title: "Erreur",
        text: "Une erreur est survenue lors de la mise à jour",
        icon: "error",
        timer: 3000
      });
    }
  });
}

 
}
