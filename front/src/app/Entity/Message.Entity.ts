import { Developpeur } from './Developpeur.Entity';
import { Projet } from './SaveProjet.Entity';

export class Message {
  constructor(
    public id?: number,
    public contenu?: string,
    public dateEnvoi?: Date,
    public expediteur?: Partial<Developpeur>,
    public projet?: Partial<Projet>,
   public destinataire?: Partial<Developpeur>, // Ajoutez Partial<>
    public prive?: boolean
  
  ) {}
}