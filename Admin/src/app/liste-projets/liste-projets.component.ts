import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { Projet, } from '../Entity/Projet.Entity';
import { CrudService } from '../service/crud.service';
@Component({
  selector: 'app-liste-projets',
  templateUrl: './liste-projets.component.html',
  styleUrls: ['./liste-projets.component.css']
})
export class ListeProjetsComponent {

  listeProjet: Projet[] = [];

  constructor(private service: CrudService, private router: Router) {}

  ngOnInit(): void {
    this.service.getProjets().subscribe(projets => {
      this.listeProjet = projets;
    });
  }

  DeleteProjet(projet: Projet) {
    if(confirm(`Voulez-vous supprimer le projet "${projet.nom}" avec l'ID ${projet.id} ?`)) {
      this.service.onDeleteProjet(projet.id).subscribe(() => {
        this.router.navigate(['/listeProjet']).then(() => {
          window.location.reload();
        });
      });
    }
  }

  updateProjetStatut(projet: Projet) {
    const newStatut = prompt(
      `Changer le statut du projet "${projet.nom}"\n\n` +
      'Options disponibles:\n' +
      '1. EN_COURS\n' +
      '2. TERMINE\n' +
      '3. ANNULE\n\n' +
      'Entrez le nouveau statut:',
      projet.statut
    );
  
    if (newStatut && ['EN_COURS', 'TERMINE', 'ANNULE'].includes(newStatut)) {
      this.service.updateProjetStatut(projet.id, newStatut).subscribe(updatedProjet => {
        const index = this.listeProjet.findIndex(p => p.id === projet.id);
        if (index !== -1) {
          this.listeProjet[index] = updatedProjet;
        }
        alert('Statut mis à jour avec succès!');
      });
    } else if (newStatut) {
      alert('Statut invalide! Les options sont: EN_COURS, TERMINE, ANNULE');
    }
  }
 
  
}