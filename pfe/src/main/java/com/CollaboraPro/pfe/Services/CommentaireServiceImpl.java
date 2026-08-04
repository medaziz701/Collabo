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
        CodePart codePart = codePartRepository.findById(codePartId)
                .orElseThrow(() -> new RuntimeException("CodePart introuvable"));

        Projet projet = codePart.getProjet();
        Equipe equipe = projet.getEquipe();
        ChefEquipe chefEquipe = equipe.getChefEquipe();

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
