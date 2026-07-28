import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Developpeur } from '../Entity/Developpeur';
import { ChefEquipe } from '../Entity/ChefEquipe.Entity';
import { Client } from '../Entity/Client.Entity';
import { JwtHelperService } from '@auth0/angular-jwt';




import { Observable } from 'rxjs';
import { Projet } from '../Entity/Projet.Entity';
import { Contact } from '../Entity/Contact.Entity';

@Injectable({
  providedIn: 'root'
})
export class CrudService {// haka rbatna angular b spring boot
  apiUrl="http://localhost:8081/api"//ay haja ndeclariwha ta7t export
  loginUserUrl="http://localhost:8081/api/admin/login"

  constructor(private http:HttpClient) { }//http hedhi jet bfadhel fel module "HttpClientModule" 
 


  getDeveloppeur(): Observable<Developpeur[]>{
    return this.http.get<Developpeur[]>(this.apiUrl +"/developpeur");
  }
  getEquipe(): Observable<Developpeur[]>{
    return this.http.get<Developpeur[]>(this.apiUrl +"/equipe");
  }
  onDeleteDeveloppeur(id : number){
    const url =`${this.apiUrl+"/developpeur"}/${id}` 
    return this.http.delete(url )
  }
  addChefEquipe(chefEquipe:ChefEquipe){
    return this.http.post<any>(this.apiUrl+"/chefEquipe", chefEquipe);
  }
  getChefEquipe(): Observable<ChefEquipe[]>{
    return this.http.get<ChefEquipe[]>(this.apiUrl +"/chefEquipe");}
    
  onDeleteChefEquipe(id : number){
    const url =`${this.apiUrl+"/chefEquipe"}/${id}` 
    return this.http.delete(url )
  }

 
  getClient(): Observable<Client[]>{
    return this.http.get<Client[]>(this.apiUrl +"/client");
  }
  onDeleteClient(id : number){
    const url =`${this.apiUrl+"/client"}/${id}` 
    return this.http.delete(url )
  }

  updateDeveloppeur(developpeur: Developpeur,id:number) {
    const url = `${this.apiUrl+"/developpeur"}/${id}`
    return this.http.put<any>(url,developpeur);
  }

  updateChefEquipe(chefEquipe: ChefEquipe,id:number) {
    const url = `${this.apiUrl+"/chefEquipe"}/${id}`
    return this.http.put<any>(url,chefEquipe);
  }
  updateClient(chefEquipe: ChefEquipe,id:number) {
    const url = `${this.apiUrl+"/client"}/${id}`
    return this.http.put<any>(url,chefEquipe);
  }

  getProjets(): Observable<Projet[]> {
    return this.http.get<Projet[]>(this.apiUrl + "/projet");
  }
  
  onDeleteProjet(id: number) {
    const url = `${this.apiUrl}/projet/${id}`;
    return this.http.delete(url);
  }

  updateProjetStatut(id: number, statut: string): Observable<Projet> {
    const url = `${this.apiUrl}/projet/${id}/update-statut-manuel`;
    return this.http.put<Projet>(url, { statut });
  }

  getContact(): Observable<Contact[]> {
    return this.http.get<Contact[]>(this.apiUrl + "/contact");
  }
  
  onDeleteContact(id: number) {
    const url = `${this.apiUrl}/contact/${id}`;
    return this.http.delete(url);
  }


loginAdmin(admin: any) {
  return this.http.post<any>(this.loginUserUrl, admin);
}

isAdmin(): boolean {
    let token = localStorage.getItem("myTokenAdmin");
    return token != null;
  }


getAdmin() {
  var token = localStorage.getItem("myTokenAdmin");
  const helper = new JwtHelperService();
  const decodedToken = helper.decodeToken(token);
  return decodedToken?.data; 
}




getNotifications(): Observable<any> {
  return this.http.get<any>(this.apiUrl + "/notifications");
}



markAsRead(id: number): Observable<void> {
  return this.http.put<void>(`${this.apiUrl}/notifications/${id}/marquer-lue`, {});
}

markAllAsRead(): Observable<void> {
  return this.http.put<void>(`${this.apiUrl}/notifications/marquer-toutes-lues`, {});
}




}

