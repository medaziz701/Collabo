import { Tache } from './tache.Entity';
import { Projet } from './SaveProjet.Entity';

export class CodePart {
  constructor(
    public id?: number,
    public filename?: string,
    public content?: string,
    public author?: string,
    public tache?: Partial<Tache>,
   public projet?: Partial<Projet>
  ) {}
}
