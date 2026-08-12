import { Component, OnInit } from '@angular/core';
import { Message } from '../Entity/Message.Entity';
import { Developpeur } from '../Entity/Developpeur.Entity';
import { CrudService } from '../service/crud.service';
import { CommonModule } from '@angular/common';
import { Tache } from '../Entity/tache.Entity';
import { FormsModule } from '@angular/forms';

@Component({
  selector: 'app-project-chat',
  templateUrl: './project-chat.component.html',
  styleUrls: ['./project-chat.component.css'],
  
})
export class ProjectChatComponent implements OnInit { 
  messages: Message[] = [];
  nouveauMessage: string = '';//nouveauMessage: string = '';
  projetId: number | null = null;//Variable pour stocker le texte du nouveau message que l'utilisateur tape.
  currentUser: Partial<Developpeur>;//Objet partiel représentant l’utilisateur connecté (développeur) Partial veut dire que toutes les propriétés sont optionnelles.
  taches: Tache[] = [];

  constructor(private crudService: CrudService) {
    this.currentUser = this.crudService.getDeveloppeurInfo() || {};//Récupération des infos du développeur connecté via le service. Si aucune info, initialisé à un objet vide.

  }

  ngOnInit(): void {
    this.chargerTachesEtProjet();
  }

  chargerTachesEtProjet(): void {
    // Récupérer les tâches du développeur connecté
    this.crudService.getTaches().subscribe(
      (taches) => {
        this.taches = taches.filter(t => t.assigneA?.id === this.currentUser.id);
        //Filtre les tâches pour ne garder que celles assignées à l’utilisateur connecté.
        // Prendre le premier projet trouvé 
        if (this.taches.length > 0 && this.taches[0].projet?.id) {
          this.projetId = this.taches[0].projet.id;
          this.loadMessages();
          setInterval(() => this.loadMessages(), 5000); //Lance un timer (setInterval) pour rafraîchir les messages toutes les 5 secondes
        }
      },
      (error) => console.error('Erreur lors du chargement des tâches', error)
    );
  }

  loadMessages(): void {
    if (!this.projetId) return;
    
    this.crudService.getMessagesByProjet(this.projetId).subscribe(
      (messages) => this.messages = messages,
      (error) => console.error('Erreur lors du chargement des messages', error)
    );
  }

  envoyerMessage(): void {
    if (!this.nouveauMessage.trim() || !this.projetId) return;
  
    const message: Message = {
      contenu: this.nouveauMessage,
      expediteur: { id: this.currentUser.id },
      projet: { id: this.projetId },
      dateEnvoi: new Date()
    };
  
    this.crudService.envoyerMessage(message).subscribe(
      (savedMessage) => {
        this.messages.push(savedMessage);
        this.nouveauMessage = '';
      },
      (error) => console.error('Erreur lors de l\'envoi du message', error)
    );
  }
}