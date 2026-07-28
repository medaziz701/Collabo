import { HttpClient, HttpParams } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Developpeur } from '../Entity/Developpeur.Entity';
import { Contact } from '../Entity/Contact.Entity';
import { Client } from '../Entity/Client.Entity';
import { ChefEquipe } from '../Entity/ChefEquipe.Entity';
import { Projet } from '../Entity/SaveProjet.Entity';
import { Tache } from '../Entity/tache.Entity';
import {  Observable,} from 'rxjs';
import { JwtHelperService } from '@auth0/angular-jwt';
import { CodePart } from '../Entity/CodePart.Entity';
import { Message } from '../Entity/Message.Entity';
import { PlanningC } from '../Entity/PlanningC.Entity';



@Injectable({
  providedIn: 'root'
})
export class CrudService {
  apiUrl = "http://localhost:8081/api";
  loginUserUrldev = "http://localhost:8081/api/developpeur/login";
  loginUserUrlCli = "http://localhost:8081/api/client/login";
  loginUserUrlChef = "http://localhost:8081/api/chefEquipe/login";
  GoogleUrl = "http://localhost:8081/api/client/login-google";

  
  tachesDeveloppeurs: { [key: number]: string } = {};

  constructor(private http: HttpClient) { }

  addDeveloppeur(developpeur: Developpeur) {
    return this.http.post<any>(this.apiUrl + "/developpeur", developpeur);
  }

  addContact(contact: Contact) {
    return this.http.post<any>(this.apiUrl + "/contact", contact);
  }

  getContact(): Observable<Contact[]> {
    return this.http.get<Contact[]>(this.apiUrl + "/contact");
  }

  onDeleteContact(id: number) {
    const url = `${this.apiUrl + "/contact"}/${id}`;
    return this.http.delete(url);
  }

  findContactById(id: number): Observable<Contact> {
    const url = `${this.apiUrl + "/contact"}/${id}`;
    return this.http.get<Contact>(url);
  }

  updateContact(id: number, contact: Contact) {
    const url = `${this.apiUrl + "/contact"}/${id}`;
    return this.http.put<any>(url, contact);
  }

  getDeveloppeur(): Observable<Developpeur[]> {
    return this.http.get<Developpeur[]>(this.apiUrl + "/developpeur");
  }

  onDeleteDeveloppeur(id: number) {
    const url = `${this.apiUrl + "/developpeur"}/${id}`;
    return this.http.delete(url);
  }

  loginDeveloppeur(developpeur: Developpeur) {
    return this.http.post<any>(this.loginUserUrldev, developpeur);
  }

  loginClient(client: Client) {
    return this.http.post<any>(this.loginUserUrlCli, client);
  }

  loginChefEquipe(chefEquipe: ChefEquipe) {
    return this.http.post<any>(this.loginUserUrlChef, chefEquipe);
  }

  findDeveloppeurById(id: number): Observable<Developpeur> {
    const url = `${this.apiUrl + "/developpeur"}/${id}`;
    return this.http.get<Developpeur>(url);
  }

  updateDeveloppeur(id: number, developpeur: Developpeur) {
    const url = `${this.apiUrl + "/developpeur"}/${id}`;
    return this.http.put<any>(url, developpeur);
  }

  isChefEquipeIn(): boolean {
    let token = localStorage.getItem("myTokenChefEquipe");
    return token != null;
  }

  isEquipeIn(): boolean {
    let token = localStorage.getItem("myTokenEquipe");
    return token != null;
  }

  isDeveloppeurIn(): boolean {
    let token = localStorage.getItem("myTokenDeveloppeur");
    return token != null;
  }

  isClientIn(): boolean {
    let token = localStorage.getItem("myTokenClient");
    return token != null;
  }

  getUserInfo() {
    var token = localStorage.getItem("myToken");
    const helper = new JwtHelperService();
    const decodedToken = helper.decodeToken(token);
    return decodedToken?.data;
  }
  getClientInfo() {
    var token = localStorage.getItem("myTokenClient");
    const helper = new JwtHelperService();
    const decodedToken = helper.decodeToken(token);
    return decodedToken?.data;
  }
  getChefEquipeInfo() {
    var token = localStorage.getItem("myTokenChefEquipe");
    const helper = new JwtHelperService();
    const decodedToken = helper.decodeToken(token);
    return decodedToken?.data;
  }
  getDeveloppeurInfo() {
    var token = localStorage.getItem("myTokenDeveloppeur");
    const helper = new JwtHelperService();
    const decodedToken = helper.decodeToken(token);
    return decodedToken?.data;
  }

  getProjet(): Observable<Projet[]> {
    return this.http.get<Projet[]>(this.apiUrl + "/projet");
  }

addProjet(projet: any): Observable<any> {
  return this.http.post<any>(this.apiUrl + "/projet", projet);
}

  getClient(): Observable<Client[]> {
    return this.http.get<Client[]>(this.apiUrl + "/client");
    
  }

  

  
  getClientById(id: number): Observable<Client> {
    return this.http.get<Client>(`${this.apiUrl}/client/${id}`);
  }

  getChefEquipeById(id: number): Observable<ChefEquipe> {
    return this.http.get<ChefEquipe>(`${this.apiUrl}/chefEquipe/${id}`);
  }

  getDeveloppeursDisponibles(): Observable<Developpeur[]> {
    return this.http.get<Developpeur[]>(`${this.apiUrl}/developpeur/disponibles`);
  }
  
 

 
  

  updateTacheForDeveloppeur(devId: number, description: string) {
    this.tachesDeveloppeurs[devId] = description;
  }

  getEquipeByProjet(id: number): Observable<any> {
    return this.http.get<any>(`${this.apiUrl}/projet/${id}/equipe`);
  }

  getTachesByProjet(id: number): Observable<Tache[]> {
    return this.http.get<Tache[]>(`${this.apiUrl}/projet/${id}/taches`);
  }
  
getTaches(): Observable<Tache[]> {
  return this.http.get<Tache[]>(`${this.apiUrl}/tache`);
}

  getAllProjetsbyChefEquipeId(){
    return this.http.get<any>( "http://localhost:8081/api/projet/get-all-by-id-ChefEquipe/"+this.getChefEquipeInfo()?.id );
  }

  addClient(client:Client){
    return this.http.post<any>(this.apiUrl+"/client", client);
  }
  onDeleteProjet(id : number){
    const url =`${this.apiUrl+"/projet"}/${id}` 
    return this.http.delete(url )
  }
  
getProjetsByClientId(id: number): Observable<any> {
  return this.http.get(`${this.apiUrl}/projet/client/${id}`);
}

signInWithGoogle(idToken: string): Observable<any> {
  const params = new HttpParams().set('id_token', idToken);
  return this.http.post(this.GoogleUrl, null, { params });

}

addFeedback(feedbackData: any): Observable<any> {
  return this.http.post(`${this.apiUrl}/feedback`, feedbackData);
}





updateChefEquipe(id: number, chefEquipe: ChefEquipe): Observable<any> {
  return this.http.put(`${this.apiUrl}/chefEquipe/${id}`, chefEquipe);
}


getTacheById(id: number): Observable<Tache> {
  return this.http.get<Tache>(`${this.apiUrl}/tache/${id}`);
}


updateCodePart(id: number, codePart: any) {
  return this.http.put(`${this.apiUrl}/code/${id}`, codePart);
}
deleteCodePart(id: number) {
  return this.http.delete(`${this.apiUrl}/code/${id}`);
}


getCodePartsByProjet(projetId: number): Observable<CodePart[]> {
  return this.http.get<CodePart[]>(`${this.apiUrl}/code/projet/${projetId}`);
}


getCodePartsByTache(tacheId: number): Observable<CodePart[]> {
  return this.http.get<CodePart[]>(`${this.apiUrl}/code/tache/${tacheId}`);
}

addCodePart(codePart: any): Observable<any> {
  // Ajoutez un log pour vérifier la requête
  console.log("Envoi à l'API:", codePart);
  return this.http.post(`${this.apiUrl}/code`, codePart);
}

marquerTacheTerminee(tacheId: number): Observable<Tache> {
  return this.http.put<Tache>(`${this.apiUrl}/tache/${tacheId}/terminer`, {});
}

toutesTachesTerminees(projetId: number): Observable<boolean> {
  return this.http.get<boolean>(`${this.apiUrl}/tache/projet/${projetId}/toutesTerminees`);
}




getMessagesByProjet(projetId: number): Observable<Message[]> {
  return this.http.get<Message[]>(`${this.apiUrl}/messages/projet/${projetId}`);
}


envoyerMessage(message: Message): Observable<Message> {
  return this.http.post<Message>(`${this.apiUrl}/messages`, message);
}

addPlanningC(planningC: PlanningC) {
  return this.http.post<any>(this.apiUrl + "/planningC", planningC);
}


getPlanningC(id: number): Observable<PlanningC[]> {
  const url = `${this.apiUrl}/planningC/get-all-by-id-chefEquipe/${id}`;
  return this.http.get<PlanningC[]>(url);
}

updateClient(id: number, client: any): Observable<any> {
  return this.http.put(`${this.apiUrl}/client/${id}`, client);
}



getDeveloppeurById(id: number): Observable<Developpeur> {
  return this.http.get<Developpeur>(`${this.apiUrl}/developpeur/${id}`);
}


updateProjetStatut(id: number, statut: string): Observable<Projet> {
  const url = `${this.apiUrl}/projet/${id}/update-statut-manuel`;
  return this.http.put<Projet>(url, { statut });
}




modifierTacheProjet(projetId: number, tacheId: number, tache: Tache): Observable<Tache> {
  return this.http.put<Tache>(
    `${this.apiUrl}/projet/${projetId}/taches/${tacheId}`, 
    tache
  );
}
ajouterMembreAEquipe(equipeId: number, developpeurId: number): Observable<any> {
  return this.http.post(`${this.apiUrl}/equipes/${equipeId}/membres`, { developpeurId });
}


updateDisponibilite(id: number, disponible: boolean): Observable<any> {
  return this.http.put(`${this.apiUrl}/developpeur/${id}/disponibilite`, disponible);
}

updateProjet(id: number, data: any): Observable<any> {
  return this.http.put(`${this.apiUrl}/projet/${id}`, data);
}




retirerMembreDeEquipe(equipeId: number, developpeurId: number): Observable<any> {
  return this.http.delete(`${this.apiUrl}/equipe/${equipeId}/membres/${developpeurId}`);
}


getProjetById(id: number): Observable<Projet> {
  return this.http.get<Projet>(`${this.apiUrl}/projet/${id}`);
}


ajouterTacheAuProjet(projetId: number, tache: Tache): Observable<Tache> {
  return this.http.post<Tache>(`${this.apiUrl}/projet/${projetId}/taches`, tache);
}


// Pour modifier une tâche
modifierTache(tacheId: number, tache: Tache): Observable<Tache> {
  return this.http.put<Tache>(`${this.apiUrl}/tache/${tacheId}`, tache);
}

// Pour supprimer une tâche
deleteTache(tacheId: number): Observable<void> {
  return this.http.delete<void>(`${this.apiUrl}/tache/${tacheId}`);
}


getPlanningByDeveloppeurId(developpeurId: number): Observable<any[]> {
  return this.http.get<any[]>(`${this.apiUrl}/planningC/get-by-developpeur-id/${developpeurId}`);
}


// Créer un commentaire
createComment(codePartId: number, contenu: string, auteur: string): Observable<any> {
  const body = { contenu, auteur, codePartId };
  return this.http.post(`${this.apiUrl}/commentaires`, body);
}

// Récupérer les commentaires par code
getCommentsByCodePart(codePartId: number): Observable<any[]> {
  return this.http.get<any[]>(`${this.apiUrl}/commentaires/code/${codePartId}`);
}



getChefIdByProjet(projetId: number): Observable<number> {
  return this.http.get<number>(`${this.apiUrl}/projet/${projetId}/chef-id`);
}




getCommentsByTache(tacheId: number): Observable<any[]> {
  return this.http.get<any[]>(`${this.apiUrl}/commentaires/tache/${tacheId}`);
}



}
