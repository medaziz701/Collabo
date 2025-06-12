package com.CollaboraPro.pfe.Services;

import com.CollaboraPro.pfe.Entity.Equipe;

import java.util.List;
import java.util.Optional;

public interface EquipeService {
    Equipe ajouterEquipe(Equipe equipe);
    Equipe modifierEquipe(Equipe equipe);
    void supprimerEquipe(Long id);
    List<Equipe> afficherEquipe();
    Optional<Equipe> afficherEquipeById(Long id);
    List<Equipe> trouverEquipesParChef(Long chefId);
    void ajouterMembreAEquipe(Long equipeId, Long developpeurId);
    void supprimerMembreDeEquipe(Long equipeId, Long developpeurId);

}
