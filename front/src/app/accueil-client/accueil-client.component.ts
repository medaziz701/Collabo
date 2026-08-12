import { Component, OnInit } from '@angular/core';
import { CrudService } from '../service/crud.service';
import { Projet } from '../Entity/SaveProjet.Entity';
import { Tache } from '../Entity/tache.Entity';

@Component({
  selector: 'app-accueil-client',
  templateUrl: './accueil-client.component.html',
  styleUrls: ['./accueil-client.component.css']
})
export class AccueilClientComponent implements OnInit { //Déclare que le composant doit implémenter ngOnInit()
  projets: Projet[] = [];
  taches: Tache[] = [];
  projetsAvecTaches: any[] = [];

  constructor(private service: CrudService) {}

  ngOnInit(): void {
    this.loadProjets();
  }

  loadProjets(): void {
    const clientId = this.service.getClientInfo()?.id;
    if (clientId) {
      this.service.getProjetsByClientId(clientId).subscribe(
        (data: any) => {
          this.projets = data;
          this.loadTaches();
        },
        error => {
          console.error('Erreur lors du chargement des projets:', error);
        }
      );
    }
  }

  loadTaches(): void {
    this.service.getTaches().subscribe(
      (data: Tache[]) => {
        this.taches = data;
        this.prepareProjetsAvecTaches();
      },
      error => {
        console.error('Erreur lors du chargement des tâches:', error);
      }
    );
  }

  prepareProjetsAvecTaches(): void {
    this.projetsAvecTaches = this.projets.map(projet => {
      const tachesProjet = this.taches.filter(tache => tache.projet?.id === projet.id);
      const tachesTerminees = tachesProjet.filter(tache => tache.statut === 'TERMINEE');
      const tachesEnCours = tachesProjet.filter(tache => tache.statut !== 'TERMINEE');

      return {
        projet: projet,
        taches: tachesProjet,
        tachesTerminees: tachesTerminees,
        tachesEnCours: tachesEnCours,
        pourcentageTermine: (tachesProjet.length > 0) ? (tachesTerminees.length / tachesProjet.length) * 100 : 0
      };
    });
  }

  getTacheClass(tache: Tache): string {
    return tache.statut === 'TERMINEE' ? 'completed' : 'pending';
  }
}