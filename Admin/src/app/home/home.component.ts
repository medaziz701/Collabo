import { Component } from '@angular/core';
import { CrudService } from '../service/crud.service';
import { Router } from '@angular/router';
import { DomSanitizer, SafeResourceUrl } from '@angular/platform-browser';



@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.css']
})
export class HomeComponent {
  powerBiUrl: SafeResourceUrl;
  
  // Vos variables existantes
  tot_chefEquipe: number = 0;
  tot_developpeur: number = 0;
  tot_equipe: number = 0;
  tot_client: number = 0;
  tot_projet: number = 0;

  // Configuration Power BI
  
  constructor(
    private service: CrudService, 
    private router: Router,
    private sanitizer: DomSanitizer
  ) {
    this.powerBiUrl = this.sanitizer.bypassSecurityTrustResourceUrl(
      'https://app.powerbi.com/reportEmbed?reportId=47c89aa5-63f3-47bb-909d-539cb2aa1821&autoAuth=true&ctid=dbd6664d-4eb9-46eb-99d8-5c43ba153c61'
    );
  }
  

  ngOnInit(): void {
    this.loadTotals();
    
  }
  
  loadTotals() {
    this.service.getProjets().subscribe(
      Projet => this.tot_projet = Projet.length
    );

    this.service.getChefEquipe().subscribe(
      ChefEquipe => this.tot_chefEquipe = ChefEquipe.length
    );

    this.service.getClient().subscribe(
      Client => this.tot_client = Client.length
    );

    this.service.getDeveloppeur().subscribe(
      Developpeur => this.tot_developpeur = Developpeur.length
    );
    
    this.service.getEquipe().subscribe(
      Equipe => this.tot_equipe = Equipe.length
    );
  }
}