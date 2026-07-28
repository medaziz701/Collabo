export class Developpeur {
    constructor(
        public id?: number,
        public nom?: string,
        public prenom?: string,
        public tlf?: string,
        public cin?: string,
        public email?: string,
        public mp?: string, 
        public adresse?: string,
        public specialite?: string,
        public etat: boolean = false, 
        public disponibilite: boolean = true, 
        public genre?: string
    ) {}
}