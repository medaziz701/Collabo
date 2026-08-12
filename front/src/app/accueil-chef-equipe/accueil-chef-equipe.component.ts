import { Component, OnInit } from '@angular/core';
import { CrudService } from '../service/crud.service';
import { Projet } from '../Entity/SaveProjet.Entity';
import { Tache } from '../Entity/tache.Entity';

@Component({
  selector: 'app-accueil-chef-equipe',
  templateUrl: './accueil-chef-equipe.component.html',
  styleUrls: ['./accueil-chef-equipe.component.css']
})
export class AccueilChefEquipeComponent implements OnInit {
  projets: Projet[] = [];
  taches: Tache[] = [];
  projetsAvecTaches: any[] = [];

   selectedTacheId: number ;
  tacheComments: any[] = []; 
  tache: any; 

  constructor(private service: CrudService) {}

  ngOnInit(): void {
    this.loadProjets();
  }

  
showComments(tache: any) { // Prendre l'objet tache complet en paramètre
  if (this.selectedTacheId === tache.id) {
    this.selectedTacheId = null;
    this.tacheComments = [];
    return;
  }

  this.selectedTacheId = tache.id;
  
  
  const nomCompletDev = tache.assigneA ? 
    `${tache.assigneA.nom} ${tache.assigneA.prenom}` : 'Auteur inconnu';

 
  this.service.getCommentsByTache(tache.id).subscribe({
    next: (comments) => {
      this.tacheComments = comments.filter(comment => 
        comment.auteur === nomCompletDev
      );
    },
    error: (err) => console.error('Erreur:', err)
  });
}
  loadProjets(): void {
    this.service.getAllProjetsbyChefEquipeId().subscribe(
      (data: any) => {
        this.projets = data;
        this.loadTaches();
      },
      error => {
        console.error('Erreur lors du chargement des projets:', error);
      }
    );
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
 
getCompletedTasksCount(taches: any[]): number {
  return taches.filter(tache => tache.statut === 'TERMINE').length;
}

getPendingTasksCount(taches: any[]): number {
  return taches.filter(tache => tache.statut === 'EN_COURS').length;
}


  getTacheClass(tache: Tache): string {
    return tache.statut === 'TERMINEE' ? 'completed' : 'pending';
}
}
