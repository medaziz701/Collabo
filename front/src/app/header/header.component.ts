import { Component, OnInit, HostListener } from '@angular/core';
import { CrudService } from '../service/crud.service';
import { Router } from '@angular/router';

@Component({
  selector: 'app-header',
  templateUrl: './header.component.html',
  styleUrls: ['./header.component.css']
})
export class HeaderComponent implements OnInit {
  isChefEquipeIn: boolean;
  isDeveloppeurIn: boolean;
  isClientIn: boolean;
  userInfo: any;
  userType: string = '';
  showProfileMenu: boolean = false;
  
  

currentUserType: string;

  constructor(private service: CrudService, private router: Router) { }

  ngOnInit(): void {
    this.isChefEquipeIn = this.service.isChefEquipeIn();
    this.isDeveloppeurIn = this.service.isDeveloppeurIn();
    this.isClientIn = this.service.isClientIn();
    
    this.determineUserType();
    this.loadUserInfo();

  
  }



  determineUserType(): void {
    if (this.isDeveloppeurIn) {
      this.userType = 'developpeur';
    } else if (this.isClientIn) {
      this.userType = 'client';
    } else if (this.isChefEquipeIn) {
      this.userType = 'chefEquipe';
    }
  }

  loadUserInfo(): void {
    switch (this.userType) {
      case 'developpeur':
        this.userInfo = this.service.getDeveloppeurInfo();
        break;
      case 'client':
        this.userInfo = this.service.getClientInfo();
        break;
      case 'chefEquipe':
        this.userInfo = this.service.getChefEquipeInfo();
        break;
    }
  }

  toggleProfileMenu(): void {
    this.showProfileMenu = !this.showProfileMenu;
  }

  @HostListener('document:click', ['$event'])
  clickOutside(event: Event) {
    if (!(event.target as HTMLElement).closest('.user-profile-dropdown')) {
      this.showProfileMenu = false;
    }
  }

  logout() {
    console.log("logout");
    localStorage.clear();
    this.router.navigate(['/']).then(() => {
      window.location.reload();
    });
  }
}