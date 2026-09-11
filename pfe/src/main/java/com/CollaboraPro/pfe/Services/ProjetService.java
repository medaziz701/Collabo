package com.CollaboraPro.pfe.Services;

import com.CollaboraPro.pfe.Entity.Equipe;
import com.CollaboraPro.pfe.Entity.Projet;
import com.CollaboraPro.pfe.Entity.SaveProjet;
import com.CollaboraPro.pfe.Entity.Tache;

import java.util.List;
import java.util.Optional;

public interface ProjetService {
    Projet ajouterProjet(SaveProjet projet);

    List<Projet> afficherProjet();
    Equipe creerEquipe(String nomEquipe, String description, String domaineSpecialisation,
                       Long chefEquipeId, List<Long> developpeursIds);
    Equipe ajouterMembreAEquipe(Long equipeId, Long developpeurId);
    Equipe retirerMembreDeEquipe(Long equipeId, Long developpeurId);
    List<Projet> getProjetByClient(Long id);
    public Tache ajouterTacheAuProjet(Long projetId, Tache nouvelleTache);
    public void supprimerProjet(Long id);
    public Tache modifierTacheProjet(Long projetId, Long tacheId, Tache tacheModifiee);
    List<Projet> getProjetByChefEquipe(Long id);
    public Projet modifierProjet(Long id, SaveProjet model);
    void updateProjetStatut(Long id);
    Projet updateProjetStatut(Long projetId, String nouveauStatut);

    public Optional<Projet> afficherProjetById(Long id);
}
