package com.CollaboraPro.pfe.Services;
import com.CollaboraPro.pfe.Entity.Contact;

import java.util.List;
import java.util.Optional;

public interface ContactService {

    Contact ajouterContact(Contact contact);
    void supprimerContact(Long id);
    List<Contact> afficherContact();

}
