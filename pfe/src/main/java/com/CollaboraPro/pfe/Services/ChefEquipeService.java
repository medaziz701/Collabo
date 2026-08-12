package com.CollaboraPro.pfe.Services;

import com.CollaboraPro.pfe.Entity.ChefEquipe;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Optional;


public interface ChefEquipeService {


    ChefEquipe  ajouterChefEquipe(ChefEquipe chefEquipe);
    ChefEquipe modifierChefEquipe(ChefEquipe chefEquipe);
    void supprimerChefEquipe(Long id);
    List<ChefEquipe> afficherChefEquipe();
    Optional<ChefEquipe> afficherChefEquipeById(Long id);




}
