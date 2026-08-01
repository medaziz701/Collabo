package com.CollaboraPro.pfe.Repository;

import com.CollaboraPro.pfe.Entity.Projet;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface ProjetRepository extends JpaRepository<Projet,Long > {


    List<Projet> findByClientId(Long id);



    List<Projet> findByChefEquipeId(Long chefEquipeId);


    @Query("SELECT p FROM Projet p LEFT JOIN FETCH p.feedbacks")
    List<Projet> findAllWithFeedbacks();

    @Query("SELECT COUNT(p) FROM Projet p")
    long countTotalProjets();

    @Query("SELECT COUNT(p) FROM Projet p WHERE p.statut = 'EN_COURS'")
    long countProjetsEnCours();

    @Query("SELECT COUNT(p) FROM Projet p WHERE p.statut = 'TERMINE'")
    long countProjetsTermines();

    @Query("SELECT DISTINCT p FROM Projet p LEFT JOIN FETCH p.equipe e LEFT JOIN FETCH e.membres LEFT JOIN FETCH p.client LEFT JOIN FETCH p.chefEquipe")
    List<Projet> findAllWithDetails();

    @Query("SELECT DISTINCT p FROM Projet p LEFT JOIN FETCH p.equipe e LEFT JOIN FETCH e.membres LEFT JOIN FETCH p.client LEFT JOIN FETCH p.chefEquipe WHERE p.id = :id")
    Optional<Projet> findByIdWithDetails(Long id);

    @Query("SELECT DISTINCT p FROM Projet p LEFT JOIN FETCH p.equipe e LEFT JOIN FETCH e.membres LEFT JOIN FETCH p.client LEFT JOIN FETCH p.chefEquipe WHERE p.client.id = :clientId")
    List<Projet> findByClientIdWithDetails(Long clientId);

    @Query("SELECT DISTINCT p FROM Projet p LEFT JOIN FETCH p.equipe e LEFT JOIN FETCH e.membres LEFT JOIN FETCH p.client LEFT JOIN FETCH p.chefEquipe WHERE p.chefEquipe.id = :chefEquipeId")
    List<Projet> findByChefEquipeIdWithDetails(Long chefEquipeId);
}
