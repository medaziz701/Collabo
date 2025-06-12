package com.CollaboraPro.pfe.Services;

import com.CollaboraPro.pfe.Entity.Admin;
import com.CollaboraPro.pfe.Entity.ChefEquipe;
import com.CollaboraPro.pfe.Entity.ConfirmationToken;
import com.CollaboraPro.pfe.Entity.Developpeur;
import com.CollaboraPro.pfe.Repository.ChefEquipeRepository;
import com.CollaboraPro.pfe.Repository.ConfirmationTokenRepository;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ChefEquipeServiceImpl implements ChefEquipeService{

    @Autowired
    ChefEquipeRepository chefEquipeRepository;



    @Autowired
    private EmailServiceImpl emailService;


    public ChefEquipe ajouterChefEquipe(ChefEquipe chefEquipe){

       return chefEquipeRepository.save(chefEquipe);
    }




    @Override
    public ChefEquipe modifierChefEquipe(ChefEquipe chefEquipe) {
        return chefEquipeRepository.save(chefEquipe);
    }

    @Override
    public void supprimerChefEquipe(Long id) {
        chefEquipeRepository.deleteById(id);

    }

    @Override
    public List<ChefEquipe> afficherChefEquipe() {
        return chefEquipeRepository.findAll();
    }

    @Override
    public Optional<ChefEquipe> afficherChefEquipeById(Long id) {
        return chefEquipeRepository.findById(id);
    }




}
