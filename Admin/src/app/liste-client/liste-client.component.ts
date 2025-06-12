import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { Client } from '../Entity/Client.Entity';
import { CrudService } from '../service/crud.service';
import Swal from 'sweetalert2';


@Component({
  selector: 'app-liste-client',
  templateUrl: './liste-client.component.html',
  styleUrls: ['./liste-client.component.css']
})
export class ListeClientComponent {
  listeClient: Client[];

  constructor(private servive: CrudService, private router: Router) {}

  ngOnInit(): void {
    this.servive.getClient().subscribe(client => {
      this.listeClient = client;
    });
  }

  DeleteClient(client: Client) {
    if("Voulez vous supprimer ce message de admin avec l'ID " +client.id+ "?") {
      this.servive.onDeleteClient(client.id).subscribe(() => {
        this.router.navigate(['/listeClient']).then(() => {
          window.location.reload();
        });
      });
    }
  }


 updateClientEtat(client: Client) {
  let updatedClient = {
    id: client.id,
    nom: client.nom,
    prenom: client.prenom,
    tlf: client.tlf,
    email: client.email,
    cin: client.cin,
    adresse: client.adresse,
    genre: client.genre,
    etat: !client.etat // Inverse l'état actuel
   
  };

  this.servive.updateClient(updatedClient, client.id).subscribe(
    res => {
    
      client.etat = !client.etat;
      Swal.fire({
        icon: client.etat ? "success" : "error",
        title: client.etat ? "Compte activé" : "Compte désactivé",
        text: `Le compte client a été ${client.etat ? 'activé' : 'désactivé'} avec succès.`,
        timer: 3000
      });
    },
    err => {
      console.error(err);
      Swal.fire("Erreur", "Une erreur est survenue lors de la mise à jour", "error");
    }
  );
}
  
  
}

