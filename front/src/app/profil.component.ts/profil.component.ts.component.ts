import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { CrudService } from '../service/crud.service';

@Component({
  selector: 'app-profil.component.ts',
  templateUrl: './profil.component.ts.component.html',
  styleUrls: ['./profil.component.ts.component.css']
})
export class ProfilComponentTsComponent  implements OnInit {
  userData: any;
  userType: string = '';
  specialite: string = ''; // Ajout de la propriété spécialité

  constructor(
    private crudService: CrudService,
    private router: Router
  ) {}

  ngOnInit(): void {
    this.determineUserType();
    this.loadProfile();
  }

  determineUserType(): void {
    if (this.crudService.isDeveloppeurIn()) {
      this.userType = 'developpeur';
    } else if (this.crudService.isClientIn()) {
      this.userType = 'client';
    } else if (this.crudService.isChefEquipeIn()) {
      this.userType = 'chefEquipe';
    } else {
      this.router.navigate(['/login']);
    }
  }

  loadProfile(): void {
    switch (this.userType) {
      case 'developpeur':
        const devInfo = this.crudService.getDeveloppeurInfo();
        this.crudService.getDeveloppeurById(devInfo.id).subscribe(
          (data: any) => {
            this.userData = data;
            this.specialite = data.specialite; // Récupération de la spécialité
          }
        );
        break;
      case 'client':
        const clientInfo = this.crudService.getClientInfo();
        this.crudService.getClientById(clientInfo.id).subscribe(
          (data: any) => this.userData = data
        );
        break;
      case 'chefEquipe':
        const chefInfo = this.crudService.getChefEquipeInfo();
        this.crudService.getChefEquipeById(chefInfo.id).subscribe(
          (data: any) => this.userData = data
        );
        break;
    }
  }
}