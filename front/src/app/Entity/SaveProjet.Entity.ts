import { Tache } from './tache.Entity';
import { Client } from './Client.Entity'; // Importez Client si ce n'est pas déjà fait
import { ChefEquipe } from './ChefEquipe.Entity'; 
import { Developpeur } from './Developpeur.Entity';

export interface Equipe {
  id?: number;
  nomEquipe?: string;
  description?: string;
  domaineSpecialisation?: string;
  membres?: Developpeur[]; 

} 

export class Projet {
  id?: number;
  nom?: string;
  datedeb?: string;
  datefin?: string;
  description?: string;
  statut?: string;
  img?: string;
  equipe?: Equipe;// Ajouté pour correspondre au backend
  taches?: Partial<Tache>[];
  client?: Client;
  idClient?: number;
  idChefEquipe?: number;
  developpeursIds?: number[];
  nomEquipe?: string;
  descriptionEquipe?: string;
  domaineSpecialisation?: string;
  tachesDeveloppeurs?: {[key: number]: Tache};
  chefEquipe?: ChefEquipe // 👈 Ajouté ici
}



export enum StatutProjet {
  EN_COURS = 'EN_COURS',
  TERMINE = 'TERMINE',
  ANNULE = 'ANNULE'
}

