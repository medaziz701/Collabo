import { Component, OnInit } from '@angular/core';
import { CrudService } from '../service/crud.service';
import { Contact } from '../Entity/Contact.Entity';

@Component({
  selector: 'app-liste-contact',
  templateUrl: './liste-contact.component.html',
  styleUrls: ['./liste-contact.component.css']
})
export class ListeContactComponent implements OnInit {
  listeContact: Contact[] = [];

  constructor(private service: CrudService) { }

  ngOnInit(): void {
    this.getContacts();
  }

  getContacts() {
    this.service.getContact().subscribe(
      (data: Contact[]) => {
        this.listeContact = data;
      },
      error => {
        console.error("Erreur lors de la récupération des contacts:", error);
      }
    );
  }

  DeleteContact(contact: Contact) {
    if (confirm("Voulez-vous vraiment supprimer ce contact ?")) {
      this.service.onDeleteContact(contact.id).subscribe(
        () => {
          // Rafraîchir la liste après suppression
          this.getContacts();
        },
        error => {
          console.error("Erreur lors de la suppression:", error);
        }
      );
    }
  }
}