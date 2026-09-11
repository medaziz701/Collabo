package com.CollaboraPro.pfe.Repository;

import com.CollaboraPro.pfe.Entity.Commentaire;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;


import java.util.List;

public interface CommentaireRepository extends JpaRepository<Commentaire, Long> {
    List<Commentaire> findByCodePartId(Long codePartId);
    List<Commentaire> findByChefEquipeId(Long chefEquipeId);


    @Query("SELECT c FROM Commentaire c WHERE c.codePart.tache.id = :tacheId")
    List<Commentaire> findByCodePartTacheId(@Param("tacheId") Long tacheId);
}