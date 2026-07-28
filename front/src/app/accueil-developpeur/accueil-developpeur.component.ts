import { Component, OnInit } from '@angular/core';
import { CrudService } from '../service/crud.service';
import { Projet } from '../Entity/SaveProjet.Entity';
import { Tache } from '../Entity/tache.Entity';
import { Message } from '../Entity/Message.Entity';

@Component({
  selector: 'app-accueil-developpeur',
  templateUrl: './accueil-developpeur.component.html',
  styleUrls: ['./accueil-developpeur.component.css']
})
export class AccueilDeveloppeurComponent implements OnInit {
  projets: Projet[] = [];
  taches: Tache[] = [];
  projetsAvecTaches: any[] = [];
  devInfo: any;
  chatVisible = false;
selectedDestinataire: any = null;
projetId: number | null = null;
messages: Message[] = [];
nouveauMessage = '';

  constructor(private service: CrudService) {}

  ngOnInit(): void {
    this.devInfo = this.service.getDeveloppeurInfo();
    if (this.devInfo?.id) {
      this.loadProjets();
    }
  }
 ouvrirChat(destinataire: any, projetId: number): void {
  
  this.chatVisible = true;
  this.selectedDestinataire = destinataire;
  this.projetId = projetId;
  this.chargerMessages();
}
fermerChat(): void {
  this.chatVisible = false;
  this.messages = [];
}

chargerMessages(): void {
  if (this.projetId) {
    this.service.getMessagesByProjet(this.projetId).subscribe(messages => {
      this.messages = messages.filter(m => 
        m.prive && 
        ((m.expediteur.id === this.devInfo.id && m.destinataire.id === this.selectedDestinataire.id) ||
         (m.destinataire.id === this.devInfo.id && m.expediteur.id === this.selectedDestinataire.id))
      );
    });
  }
}

envoyerMessage(): void {
  if (this.nouveauMessage.trim() && this.selectedDestinataire && this.projetId) {
    const newMessage: Message = {
      contenu: this.nouveauMessage,
      prive: true,
      expediteur: { id: this.devInfo.id },
      destinataire: { id: this.selectedDestinataire.id },
      projet: { id: this.projetId }
    };

    this.service.envoyerMessage(newMessage).subscribe(() => {
      this.nouveauMessage = '';
      this.chargerMessages();
    });
  }
}

  loadProjets(): void {
    // Récupérer tous les projets où le développeur est membre d'équipe
    this.service.getProjet().subscribe(
      (data: Projet[]) => {
        this.projets = data.filter(projet => 
          projet.equipe?.membres?.some((membre: any) => membre.id === this.devInfo.id)
        );
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

  prepareProjetsAvecTaches(): void { //Mot-clé:const	Portée:Bloc ({ ... })	Réaffectation autorisée : Non||| Mot-clé:let	Portée:Bloc ({ ... })	Réaffectation autorisée :Oui
    this.projetsAvecTaches = this.projets.map(projet => {
      const tachesProjet = this.taches.filter(tache => tache.projet?.id === projet.id);
      const tachesTerminees = tachesProjet.filter(tache => tache.statut === 'TERMINEE');
      const tachesEnCours = tachesProjet.filter(tache => tache.statut !== 'TERMINEE');
      const mesTaches = tachesProjet.filter(tache => tache.assigneA?.id === this.devInfo.id);

      return {
        projet: projet,
        taches: tachesProjet,
        mesTaches: mesTaches,
        tachesTerminees: tachesTerminees,
        tachesEnCours: tachesEnCours,
        mesTachesTerminees: mesTaches.filter(t => t.statut === 'TERMINEE'),
        mesTachesEnCours: mesTaches.filter(t => t.statut !== 'TERMINEE'),
        pourcentageTermine: (tachesProjet.length > 0) ? (tachesTerminees.length / tachesProjet.length) * 100 : 0,
        monPourcentageTermine: (mesTaches.length > 0) ? (mesTaches.filter(t => t.statut === 'TERMINEE').length / mesTaches.length) * 100 : 0
      };
    });
  }

  getTacheClass(tache: Tache): string {
    return tache.statut === 'TERMINEE' ? 'completed' : 'pending';
  }

  isMaTache(tache: Tache): boolean {
    return tache.assigneA?.id === this.devInfo.id;
  }

  marquerTacheTerminee(tacheId: number): void {
    this.service.marquerTacheTerminee(tacheId).subscribe(
      () => {
        this.loadTaches(); // Recharger les tâches après mise à jour
      },
      error => {
        console.error("Erreur lors de la mise à jour de la tâche:", error);
      }
    );
  }
}