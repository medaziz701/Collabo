package com.CollaboraPro.pfe.RestController;

import com.CollaboraPro.pfe.Entity.Admin;
import com.CollaboraPro.pfe.Entity.Contact;
import com.CollaboraPro.pfe.Entity.NotificationType;
import com.CollaboraPro.pfe.Services.ContactService;
import com.CollaboraPro.pfe.Services.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@RestController
@CrossOrigin("*")
@RequestMapping(value = "/contact")
public class ContactRestController {
    @Autowired
    ContactService contactService;
    @Autowired
    NotificationService notificationService;

    @RequestMapping(method = RequestMethod.POST )
    public Contact AjouterContact (@RequestBody Contact contact) {
        Admin admin = new Admin();
        admin.setId(1L);
        contact.setManagedByAdmin(admin);
        notificationService.creerNotification(
                admin,
                NotificationType.NOUVEAU_CONTACT,
                "Nouveau message de " + contact.getNom()

        );

        Contact savedContact = contactService.ajouterContact(contact); // Sauvegarder d'abord



        return savedContact;
    }


    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE )

    public void suppContact(@PathVariable("id") Long id){
        contactService.supprimerContact(id);

    }

    @RequestMapping(method = RequestMethod.GET )
    public List<Contact> afficherContact(){
        return contactService.afficherContact();

    }

}
