import { Projet } from './SaveProjet.Entity';
import { Developpeur } from './Developpeur.Entity';
import { CodePart } from './CodePart.Entity'; // à ajouter si tu as une entité CodePart

export enum StatutTache {
  A_FAIRE = 'A_FAIRE',
  EN_COURS = 'EN_COURS',
  TERMINEE = 'TERMINEE'
}

export class Tache {
  constructor(
    public id?: number,
    public description?: string,
    public dateCreation?: Date | string,
    public statut?: StatutTache,
    public projet?: Partial<Projet>,
    public assigneA?: Partial<Developpeur>,
    public dateLimite?:string,
  ) {}
}
