package com.CollaboraPro.pfe.Services;

import com.CollaboraPro.pfe.Entity.Commentaire;

import java.util.List;

public interface CommentaireService {

    Commentaire creerCommentaire(Long codePartId, String contenu, String auteur);
    List<Commentaire> getCommentairesParCode(Long codePartId);
    public List<Commentaire> getCommentairesPourChef(Long chefEquipeId);


    List<Commentaire> getCommentairesParTache(Long tacheId);
}
