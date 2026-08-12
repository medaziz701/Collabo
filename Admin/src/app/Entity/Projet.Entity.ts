import { Client } from './Client.Entity';
import { ChefEquipe } from './ChefEquipe.Entity';

export class Projet {
    constructor(
      public id?: number,
      public nom?: string,
      public datedeb?: string,
      public datefin?: string,
      public description?: string,
      public statut?: string,
      public img?: string,
      public equipe?: Equipe,
      public client?: Client,
      public chefEquipe?: ChefEquipe,
      
    ) {}
  }
  
  
  
  export interface Equipe {
    id: number;
    nomEquipe: string;
    description: string;
    domaineSpecialisation: string;
    dateCreation: string;
    dateDerniereModification: string;
    nombreMembres: number;
    membresInfo: string;
    chefEquipe: ChefEquipe;
  }
  

  
 