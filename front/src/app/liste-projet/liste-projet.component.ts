import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { CrudService } from '../service/crud.service';
import { Projet } from '../Entity/SaveProjet.Entity';
import { Developpeur } from '../Entity/Developpeur.Entity';

@Component({
  selector: 'app-liste-projet',
  templateUrl: './liste-projet.component.html',
  styleUrls: ['./liste-projet.component.css']
})
export class ListeProjetComponent implements OnInit {
  MesProjet: any[] = [];
  projetToEdit: any = null;
  showEditForm: boolean = false;
  listDeveloppeursDisponibles: Developpeur[] = [];
  nouveauxDeveloppeursSelectionnes: number[] = [];
  
  // Données du formulaire d'édition
  editFormData: any = {
    projet: {
      nom: '',
      datedeb: '',
      datefin: '',
      description: '',
      statut: ''
    },
    equipe: {
      nomEquipe: '',
      description: '',
      domaineSpecialisation: '',
      membresIds: []
    },
    taches: {}
  };

  constructor(private service: CrudService, private router: Router) {}

  ngOnInit(): void {
    this.loadProjets();
    this.loadDeveloppeursDisponibles();
  }

  // Charger les projets
  loadProjets(): void {
    this.service.getAllProjetsbyChefEquipeId().subscribe(
      (data: any) => {
        this.MesProjet = data.map((projet: any) => this.parseProjetData(projet));
      },
      error => {
        console.error('Erreur lors du chargement des projets:', error);
      }
    );
  }

  // Charger les développeurs disponibles
  loadDeveloppeursDisponibles(): void {
    this.service.getDeveloppeursDisponibles().subscribe(
      (devs: Developpeur[]) => {
        this.listDeveloppeursDisponibles = devs;
      },
      error => {
        console.error('Erreur lors du chargement des développeurs:', error);
      }
    );
  }

  // Ouvrir le modal d'édition
  openEditModal(projet: any): void {
    this.projetToEdit = projet;
    this.nouveauxDeveloppeursSelectionnes = [];
    
    // Préparer les données pour le formulaire
    this.editFormData = {
      projet: {
        nom: projet.nom,
        datedeb: projet.datedeb,
        datefin: projet.datefin,
        description: projet.description,
        statut: projet.statut
      },
      equipe: {
        nomEquipe: projet.equipe?.nomEquipe || '',
        description: projet.equipe?.description || '',
        domaineSpecialisation: projet.equipe?.domaineSpecialisation || '',
        membresIds: projet.equipe?.membres?.map((m: any) => m.id) || []
      },
      taches: this.prepareTachesData(projet.taches)
    };
    
    this.showEditForm = true;
  }

  // Préparer les données des tâches pour le formulaire
  getTacheDateLimite(membreId: number): string {
  // Retourne la date limite si elle existe, sinon une chaîne vide
  return this.editFormData.taches[membreId]?.dateLimite || '';
}
getSafeTacheDescription(membreId: number): string {
  // Retourne la description si elle existe, sinon une chaîne vide
  return this.editFormData.taches[membreId]?.description || '';
}



// Méthode pour supprimer un membre et ses tâches
removeMember(membreId: number): void {
  if (confirm(`Voulez-vous vraiment retirer ce membre de l'équipe ? Ses tâches seront également supprimées.`)) {
    // 1. Retirer le membre de la liste des membres de l'équipe
    this.editFormData.equipe.membresIds = this.editFormData.equipe.membresIds.filter((id: number) => id !== membreId);
    
    // 2. Supprimer les tâches associées à ce membre
    if (this.editFormData.taches[membreId]) {
      delete this.editFormData.taches[membreId];
    }
    
    // 3. Mettre à jour la liste des membres dans l'affichage
    if (this.projetToEdit.equipe?.membres) {
      this.projetToEdit.equipe.membres = this.projetToEdit.equipe.membres.filter((m: any) => m.id !== membreId);
    }
    
    // 4. Ajouter le membre à la liste des développeurs disponibles s'il n'y était pas
    const dev = this.listDeveloppeursDisponibles.find(d => d.id === membreId);
    if (!dev) {
      const removedMember = this.projetToEdit.equipe?.membres?.find((m: any) => m.id === membreId);
      if (removedMember) {
        this.listDeveloppeursDisponibles.push(removedMember);
      }
    }
  }
}

setSafeTacheDescription(membreId: number, description: string): void {
  // Initialise l'objet tache s'il n'existe pas
  if (!this.editFormData.taches[membreId]) {
    this.editFormData.taches[membreId] = { description: '', dateLimite: '' };
  }
  // Met à jour la description
  this.editFormData.taches[membreId].description = description;
}
setTacheDateLimite(membreId: number, date: string): void {
  // Initialise l'objet tache s'il n'existe pas
  if (!this.editFormData.taches[membreId]) {
    this.editFormData.taches[membreId] = {};
  }
  // Met à jour la date limite
  this.editFormData.taches[membreId].dateLimite = date;
}
  // Gérer la sélection des nouveaux développeurs
  onDeveloppeurSelectionChange(event: any, devId: number): void {
    if (event.target.checked) {
      if (!this.nouveauxDeveloppeursSelectionnes.includes(devId)) {
        this.nouveauxDeveloppeursSelectionnes.push(devId);
        // Initialiser une tâche vide pour les nouveaux membres
        this.editFormData.taches[devId] = {
          description: '',
          dateLimite: ''
        };
      }
    } else {
      this.nouveauxDeveloppeursSelectionnes = this.nouveauxDeveloppeursSelectionnes.filter(id => id !== devId);
      delete this.editFormData.taches[devId];
    }
  }

  // Soumettre le formulaire de modification
  submitEditForm(): void {
    if (!this.projetToEdit) return;

    // Fusionner les membres existants et nouveaux
    const allMembresIds = [
      ...this.editFormData.equipe.membresIds,
      ...this.nouveauxDeveloppeursSelectionnes
    ];

    // Préparer les données à envoyer
    const formData = {
      ...this.editFormData.projet,
      nomEquipe: this.editFormData.equipe.nomEquipe,
      descriptionEquipe: this.editFormData.equipe.description,
      domaineSpecialisation: this.editFormData.equipe.domaineSpecialisation,
      developpeursIds: allMembresIds,
      tachesDeveloppeurs: this.editFormData.taches
    };

    // Envoyer la requête de mise à jour
    this.service.updateProjet(this.projetToEdit.id, formData).subscribe(
      (updatedProjet: any) => {
        // Mettre à jour la liste des projets
        const index = this.MesProjet.findIndex(p => p.id === this.projetToEdit.id);
        if (index !== -1) {
          this.MesProjet[index] = this.parseProjetData(updatedProjet);
        }
        
        // Fermer le modal et réinitialiser
        this.showEditForm = false;
        this.projetToEdit = null;
        alert('Projet mis à jour avec succès!');
      },
      error => {
        console.error("Erreur lors de la mise à jour:", error);
        alert("Une erreur est survenue lors de la mise à jour du projet.");
      }
    );
  }

  // Supprimer un projet
  DeleteProjet(projet: Projet) {
    if(confirm(`Voulez-vous supprimer le projet "${projet.nom}" avec l'ID ${projet.id} ?`)) {
      this.service.onDeleteProjet(projet.id).subscribe(() => {
        this.router.navigate(['/listeProjet']).then(() => {
          window.location.reload();
        });
      });
    }
  }

  // Modifier le statut d'un projet
  updateProjetStatut(projet: any) {
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
        const index = this.MesProjet.findIndex(p => p.id === projet.id);
        if (index !== -1) {
          this.MesProjet[index] = this.parseProjetData(updatedProjet);
        }
        alert('Statut mis à jour avec succès!');
      }, error => {
        console.error("Erreur lors de la mise à jour:", error);
        alert("Une erreur est survenue lors de la mise à jour du statut.");
      });
    } else if (newStatut) {
      alert('Statut invalide! Les options sont: EN_COURS, TERMINE, ANNULE');
    }
  }

  // Parser les données des membres
  private parseProjetData(projet: any): any {
    if (projet.equipe?.membresInfo && typeof projet.equipe.membresInfo === 'string') {
      try {
        projet.equipe.membresInfo = JSON.parse(projet.equipe.membresInfo);
      } catch (e) {
        console.error("Erreur lors du parsing des membres:", e);
        projet.equipe.membresInfo = [];
      }
    }
    return projet;
  }

  

  // Vérifier si une variable est une chaîne
  isString(value: any): boolean {
    return typeof value === 'string';
  }
onTacheDateChange(membreId: number, newDate: string): void {
  if (!this.editFormData.taches[membreId]) {
    this.editFormData.taches[membreId] = {};
  }
  this.editFormData.taches[membreId].dateLimite = newDate;
}

prepareTachesData(taches: any[]): any {
  const tachesData: any = {};
  taches?.forEach(tache => {
    if (tache.assigneA) {
      tachesData[tache.assigneA.id] = {
        description: tache.description || '',
        dateLimite: tache.dateLimite || ''
      };
    }
  });
  return tachesData;
}
  // Fermer le modal
  closeEditModal(): void {
    this.showEditForm = false;
    this.projetToEdit = null;
  }
}





