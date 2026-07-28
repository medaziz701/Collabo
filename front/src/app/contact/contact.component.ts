import { Component } from '@angular/core';
import { FormGroup, FormBuilder, FormControl, Validators, AbstractControl } from '@angular/forms';
import { Router } from '@angular/router';
import { Contact } from '../Entity/Contact.Entity';
import { CrudService } from '../service/crud.service';

@Component({
  selector: 'app-ajouter-contact',
  templateUrl: './contact.component.html', 
  styleUrls: ['./contact.component.css']
})
export class ContactComponent {

  messageCommande = "";
  contactForm: FormGroup;
  
  constructor(private services: CrudService, private router: Router, private fb: FormBuilder) {
    let formControls = {
      nom: new FormControl('', [
        Validators.required,
        Validators.minLength(4)
      ]),
      prenom: new FormControl('', [
        Validators.required,
      ]),
      sujet: new FormControl('', [
        Validators.required,
      ]),
      msg: new FormControl('', [
        Validators.required,
      ]),
      tlf: new FormControl('', [
        Validators.required,
        Validators.pattern('[0-9]{8}')
      ]),
      email: new FormControl('', [
       
        Validators.email
      ]),
    }
    this.contactForm = this.fb.group(formControls);
  }
 

 

  get nom() { return this.contactForm.get('nom'); }
  get prenom() { return this.contactForm.get('prenom'); }
  get sujet() { return this.contactForm.get('sujet'); }
  get msg() { return this.contactForm.get('msg'); }
  get tlf() { return this.contactForm.get('tlf'); }
  get email() { return this.contactForm.get('email'); }

  addNewContact() {
    let data = this.contactForm.value;
    console.log(data);
    let contact = new Contact(
      undefined, data.nom, data.prenom,  data.sujet, data.msg,  data.tlf,data.email
    );
    console.log(contact);

    if (this.contactForm.invalid) {
      this.messageCommande = `<div class="alert alert-danger" role="alert">
                                Veuillez remplir tous les champs correctement
                              </div>`;
    } else {
      this.services.addContact(contact).subscribe(
        res => {
          console.log(res);
          this.messageCommande = `<div class="alert alert-success" role="alert">
                                    Message envoyé avec succès
                                  </div>`;
         
        },
        err => {
          this.messageCommande = `<div class="alert alert-warning" role="alert">
                                    Une erreur est survenue!
                                  </div>`;
        }
      );
    }
  }
}