package com.CollaboraPro.pfe.Services;


import com.CollaboraPro.pfe.Entity.*;
import com.CollaboraPro.pfe.Repository.CodePartRepository;
import com.CollaboraPro.pfe.Repository.CommentaireRepository;
import com.CollaboraPro.pfe.Repository.EquipeRepository;
import com.CollaboraPro.pfe.Repository.ProjetRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CommentaireServiceImpl implements CommentaireService{

    @Autowired
    private CommentaireRepository commentaireRepository;

    @Autowired
    private CodePartRepository codePartRepository;

    @Autowired
    private ProjetRepository projetRepository;

    @Autowired
    private EquipeRepository equipeRepository;

    public Commentaire creerCommentaire(Long codePartId, String contenu, String auteur) {
        System.out.println("creerCommentaire - codePartId: " + codePartId + ", contenu: " + contenu + ", auteur: " + auteur);

        CodePart codePart = codePartRepository.findById(codePartId)
                .orElseThrow(() -> new RuntimeException("CodePart introuvable"));

        System.out.println("CodePart found: " + codePart.getFilename());
        Projet projet = codePart.getProjet();
        System.out.println("Projet: " + (projet != null ? projet.getNom() : "null"));

        if (projet == null) {
            throw new RuntimeException("Projet is null for CodePart: " + codePartId);
        }

        Equipe equipe = projet.getEquipe();
        System.out.println("Equipe: " + (equipe != null ? equipe.getNomEquipe() : "null"));

        if (equipe == null) {
            throw new RuntimeException("Equipe is null for Projet: " + projet.getId());
        }

        ChefEquipe chefEquipe = equipe.getChefEquipe();
        System.out.println("ChefEquipe: " + (chefEquipe != null ? chefEquipe.getNom() : "null"));

        if (chefEquipe == null) {
            throw new RuntimeException("ChefEquipe is null for Equipe: " + equipe.getId());
        }

        Commentaire commentaire = new Commentaire();
        commentaire.setContenu(contenu);
        commentaire.setAuteur(auteur);
        commentaire.setCodePart(codePart);
        commentaire.setChefEquipe(chefEquipe);

        return commentaireRepository.save(commentaire);
    }

    public List<Commentaire> getCommentairesParCode(Long codePartId) {
        return commentaireRepository.findByCodePartId(codePartId);
    }

    public List<Commentaire> getCommentairesPourChef(Long chefEquipeId) {
        return commentaireRepository.findByChefEquipeId(chefEquipeId);
    }

    @Override
    public List<Commentaire> getCommentairesParTache(Long tacheId) {
        return commentaireRepository.findByCodePartTacheId(tacheId);
    }


}
