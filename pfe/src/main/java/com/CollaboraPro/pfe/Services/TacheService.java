package com.CollaboraPro.pfe.Services;

import com.CollaboraPro.pfe.Entity.Tache;

import java.util.List;
import java.util.Optional;

public interface TacheService {
    Tache ajouterTache(Tache tache);
    Tache modifierTache(Tache tache);
    void supprimerTache(Long id);
    List<Tache> afficherTaches();
    Optional<Tache> afficherTacheById(Long id);

    Boolean toutesTachesTerminees(Long projetId);

    Tache marquerTacheTerminee(Long id);
}
