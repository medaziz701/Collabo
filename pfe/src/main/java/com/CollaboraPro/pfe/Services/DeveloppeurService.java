package com.CollaboraPro.pfe.Services;

import com.CollaboraPro.pfe.Entity.ChefEquipe;
import com.CollaboraPro.pfe.Entity.Developpeur;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Optional;

public interface DeveloppeurService {
    Developpeur ajouterDeveloppeur(Developpeur developpeur);
    Developpeur modifierDeveloppeur(Developpeur developpeur,Long id);
    void supprimerDeveloppeur(Long id);
    List<Developpeur> afficherDeveloppeur();
    Optional<Developpeur> afficherDeveloppeurById(Long id);
    public List<Developpeur> findDeveloppeursDisponibles() ;
    public void updateDisponibilite(Long id, boolean disponibilite);

}
