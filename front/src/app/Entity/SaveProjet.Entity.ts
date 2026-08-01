import { Tache } from './tache.Entity';
import { Client } from './Client.Entity'; // Importez Client si ce n'est pas déjà fait
import { ChefEquipe } from './ChefEquipe.Entity'; 
import { Developpeur } from './Developpeur.Entity';

export interface Equipe {
  id?: number;
  nomEquipe?: string;
  description?: string;
  domaineSpecialisation?: string;
}

export interface MembreInfo {
  id?: number;
  nom?: string;
  prenom?: string;
}

export class Projet {
  id?: number;
  nom?: string;
  datedeb?: string;
  datefin?: string;
  description?: string;
  statut?: string;
  img?: string;
  equipe?: Equipe;
  membres?: MembreInfo[];  // Ajouté pour correspondre au backend ProjetDTO
  taches?: Partial<Tache>[];
  client?: Client;
  idClient?: number;
  idChefEquipe?: number;
  developpeursIds?: number[];
  nomEquipe?: string;
  descriptionEquipe?: string;
  domaineSpecialisation?: string;
  tachesDeveloppeurs?: {[key: number]: Tache};
  chefEquipe?: ChefEquipe;
  equipeId?: number;
  clientId?: number;
  chefEquipeId?: number;
  clientNom?: string;
  chefEquipeNom?: string;
}



export enum StatutProjet {
  EN_COURS = 'EN_COURS',
  TERMINE = 'TERMINE',
  ANNULE = 'ANNULE'
}

