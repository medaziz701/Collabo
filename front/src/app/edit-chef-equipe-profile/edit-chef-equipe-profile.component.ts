import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { CrudService } from '../service/crud.service';
import { Router } from '@angular/router';

@Component({
  selector: 'app-edit-chef-equipe-profile',
  templateUrl: './edit-chef-equipe-profile.component.html',
  styleUrls: ['./edit-chef-equipe-profile.component.css']
})
export class EditChefEquipeProfileComponent implements OnInit {
  profileForm: FormGroup;
  passwordForm: FormGroup;
  userData: any;
  successMessage: string = '';
  errorMessage: string = '';
  userType: string = '';
  showPasswordForm: boolean = false;

  constructor(
    private crudService: CrudService,
    private fb: FormBuilder,
    private router: Router
  ) {
    // Formulaire principal (sans mot de passe)
    this.profileForm = this.fb.group({
      nom: ['', Validators.required],
      prenom: ['', Validators.required],
      tlf: ['', Validators.required],
      email: ['', [Validators.required, Validators.email]],
      adresse: ['', Validators.required],
      genre: ['', Validators.required],
      mp: ['']
    });


   
  }

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
          (data: any) => this.setFormData(data)
        );
        break;
      case 'client':
        const clientInfo = this.crudService.getClientInfo();
        this.crudService.getClientById(clientInfo.id).subscribe(
          (data: any) => this.setFormData(data)
        );
        break;
      case 'chefEquipe':
        const chefInfo = this.crudService.getChefEquipeInfo();
        this.crudService.getChefEquipeById(chefInfo.id).subscribe(
          (data: any) => this.setFormData(data)
        );
        break;
    }
  }

  setFormData(data: any): void {
    this.userData = data;
    this.profileForm.patchValue({
      nom: data.nom,
      prenom: data.prenom,
      tlf: data.tlf,
      email: data.email,
      adresse: data.adresse,
      genre: data.genre,
      mp:data.mp
    });
  }

  onSubmit(): void {
    if (this.profileForm.valid) {
      const formData = this.profileForm.value;
      const updatedUser = {
        ...this.userData,
        nom: formData.nom,
        prenom: formData.prenom,
        tlf: formData.tlf,
        email: formData.email,
        adresse: formData.adresse,
        genre: formData.genre,
        mp:formData.mp
      };

      switch (this.userType) {
        case 'developpeur':
          this.updateDeveloppeur(updatedUser);
          break;
        case 'client':
          this.updateClient(updatedUser);
          break;
        case 'chefEquipe':
          this.updateChefEquipe(updatedUser);
          break;
      }
    }
  }



  updateDeveloppeur(user: any): void {
    this.crudService.updateDeveloppeur(this.userData.id, user).subscribe(
      response => this.handleSuccess(),
      error => this.handleError(error)
    );
  }

  updateClient(user: any): void {
    this.crudService.updateClient(this.userData.id, user).subscribe(
      response => this.handleSuccess(),
      error => this.handleError(error)
    );
  }

  updateChefEquipe(user: any): void {
    this.crudService.updateChefEquipe(this.userData.id, user).subscribe(
      response => this.handleSuccess(),
      error => this.handleError(error)
    );
  }

  handleSuccess(): void {
    this.successMessage = 'Profil mis à jour avec succès';
    setTimeout(() => {
      this.successMessage = '';
      this.loadProfile();
    }, 3000);
  }

  handleError(error: any): void {
    this.errorMessage = 'Erreur lors de la mise à jour du profil';
    console.error('Erreur:', error);
    setTimeout(() => this.errorMessage = '', 3000);
  }

 
}