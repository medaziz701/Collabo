package com.CollaboraPro.pfe.Services;

import com.CollaboraPro.pfe.Entity.Client;

import java.util.List;
import java.util.Optional;

public interface ClientService {

    Client ajouterClient(Client client);
    Client modifierClient(Client client);
    void supprimerClient(Long id);
    List<Client> afficherClient();
    Optional<Client> afficherClientById(Long id);
}
