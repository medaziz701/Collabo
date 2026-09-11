package com.CollaboraPro.pfe.Services;

import com.CollaboraPro.pfe.Entity.ChefEquipe;
import com.CollaboraPro.pfe.Entity.ConfirmationToken;
import com.CollaboraPro.pfe.Entity.Developpeur;
import com.CollaboraPro.pfe.Repository.ConfirmationTokenRepository;
import com.CollaboraPro.pfe.Repository.DeveloppeurRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class DeveloppeurServiceImpl implements DeveloppeurService{


    @Autowired
    DeveloppeurRepository developpeurRepository;



    @Autowired
    private EmailServiceImpl emailService;


    @Override
    public Developpeur ajouterDeveloppeur(Developpeur developpeur) {//ResponseEntity<Object> pour une réponse HTTP flexible
        return developpeurRepository.save(developpeur);
    }

    @Override
    public Developpeur modifierDeveloppeur(Developpeur developpeur , Long id) {
        return developpeurRepository.save(developpeur);

    }

    @Override
    public void supprimerDeveloppeur(Long id) {
        developpeurRepository.deleteById(id);


    }

    @Override
    public List<Developpeur> afficherDeveloppeur() {
        return developpeurRepository.findAll();
    }

    @Override
    public Optional<Developpeur> afficherDeveloppeurById(Long id) {
        return developpeurRepository.findById(id);
    }



    @Override
    public List<Developpeur> findDeveloppeursDisponibles() {
        return developpeurRepository.findByDisponibilite(true);
    }


    @Override
    public void updateDisponibilite(Long id, boolean disponibilite) {
        Developpeur dev = developpeurRepository.findById(id).orElse(null);
        if (dev != null) {
            dev.setDisponibilite(disponibilite);
            developpeurRepository.save(dev);
        }
    }



}