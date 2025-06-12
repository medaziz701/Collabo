export class Feedback {
    constructor(
        public id?: number,
        public noteEtoiles?: number, // (1-5 étoiles)
        public commentaire?: string,
        public dateCreation?: Date,
        public projet?: any, 
        public client?: any   
    ) {}
}