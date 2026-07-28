import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { CrudService } from '../service/crud.service';




@Component({
  selector: 'app-mes-projets',
  templateUrl: './mes-projets.component.html',
  styleUrls: ['./mes-projets.component.css']
})
export class MesProjetsComponent implements OnInit {
  taches: any[] = [];
  devInfo: any;
  

  constructor(private service: CrudService, private router: Router) {}

  ngOnInit(): void {
    this.devInfo = this.service.getDeveloppeurInfo();
    
    if (this.devInfo?.id) {
      this.service.getTaches().subscribe(
        (allTaches: any[]) => {
          // Filtrer et parser les données
          this.taches = allTaches
            .filter(tache => tache.assigneA?.id === this.devInfo.id)
            .map(tache => this.parseTacheData(tache));
          
          console.log("Tâches assignées:", this.taches);
        },
        error => {
          console.error("Erreur lors de la récupération des tâches:", error);
        }
      );
    }
  }

  private parseTacheData(tache: any): any {

     tache.projet = tache.projet || {};
    // Parser membresInfo si c'est une string JSON
    // Parse membresInfo si nécessaire
    if (tache.projet.equipe?.membresInfo && typeof tache.projet.equipe.membresInfo === 'string') {
        try {
            tache.projet.equipe.membresInfo = JSON.parse(tache.projet.equipe.membresInfo);
        } catch (e) {
            tache.projet.equipe.membresInfo = [];
        }
    }
    
    // Assure que les objets imbriqués existent
    tache.projet.equipe = tache.projet.equipe || {};
    tache.projet.equipe.chefEquipe = tache.projet.equipe.chefEquipe || {};
    tache.projet.client = tache.projet.client || {};
    
    return tache;
}
}