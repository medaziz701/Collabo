export class Client {
   constructor(
     public id?: number,
     public nom?: string,
     public prenom?: string,
     public tlf?: string,
     public email?: string,
     public cin?: string,
     public mp?: string,
     public adresse?: string,
     public genre?: string,
     public etat: boolean = false

     
   ) {}
 }