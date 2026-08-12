export class ChefEquipe {
    constructor(
      public id?: number,
      public nom?: string,
      public prenom?: string,
      public tlf?: string,
      public email?: string,
      public adresse?: string,
      public mp?: string,
      public genre?: string,
      public cin?: string,
      public dateNaissance?: Date,
      public datePriseFonction?: Date,
      public etat?:boolean,

     
    ) {}
  }