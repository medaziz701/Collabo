package com.CollaboraPro.pfe.Services;

import com.CollaboraPro.pfe.Entity.Contact;
import com.CollaboraPro.pfe.Repository.ContactRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class ContactServiceImpl implements ContactService{

    @Autowired
    ContactRepository contactRepository;
    @Override
    public Contact ajouterContact(Contact contact) {
        contactRepository.save(contact);
        return contact;
    }

    @Override
    public void supprimerContact(Long id) {

        contactRepository.deleteById(id);
    }

    @Override
    public List<Contact> afficherContact() {

        return contactRepository.findAll();
    }
}
