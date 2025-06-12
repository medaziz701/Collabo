import { ChefEquipe } from "./ChefEquipe.Entity";

export class PlanningC {
    constructor(
        public id?: number,
        public nom?: string,
        public date?: Date,
        public debut?: string,
        public fin?: string,
        public chefEquipe?: ChefEquipe,
    ) {}}