package com.CollaboraPro.pfe.Services;

import com.CollaboraPro.pfe.Entity.Developpeur;
import com.CollaboraPro.pfe.Entity.Equipe;
import com.CollaboraPro.pfe.Repository.DeveloppeurRepository;
import com.CollaboraPro.pfe.Repository.EquipeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class EquipeServiceImpl implements EquipeService{

    @Autowired
    EquipeRepository equipeRepository;

    @Autowired
    DeveloppeurRepository developpeurRepository;

    @Override
    public Equipe ajouterEquipe(Equipe equipe) {

        return equipeRepository.save(equipe);
    }

    @Override
    public Equipe modifierEquipe(Equipe equipe) {

        return equipeRepository.save(equipe);
    }

    @Override
    public void supprimerEquipe(Long id) {
        equipeRepository.deleteById(id);

    }

    @Override
    public List<Equipe> afficherEquipe() {
        return
                equipeRepository.findAll();
    }

    @Override
    public Optional<Equipe> afficherEquipeById(Long id) {
        return equipeRepository.findById(id);    }

    @Override
    public List<Equipe> trouverEquipesParChef(Long chefId) {
        return equipeRepository.findByChefEquipe_Id(chefId);
    }


    @Override
    public void ajouterMembreAEquipe(Long equipeId, Long developpeurId) {
        Equipe equipe = equipeRepository.findById(equipeId).orElse(null);
        Developpeur developpeur = developpeurRepository.findById(developpeurId).orElse(null);

        if (equipe != null && developpeur != null && !equipe.getMembres().contains(developpeur)) {
            equipe.getMembres().add(developpeur);
            equipeRepository.save(equipe);
        }
    }

    @Override
    public void supprimerMembreDeEquipe(Long equipeId, Long developpeurId) {
        Equipe equipe = equipeRepository.findById(equipeId).orElse(null);
        Developpeur developpeur = developpeurRepository.findById(developpeurId).orElse(null);

        if (equipe != null && developpeur != null) {
            equipe.getMembres().remove(developpeur);
            equipeRepository.save(equipe);
        }
    }
}