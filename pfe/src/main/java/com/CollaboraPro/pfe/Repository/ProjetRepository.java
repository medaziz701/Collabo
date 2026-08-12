package com.CollaboraPro.pfe.Repository;

import com.CollaboraPro.pfe.Entity.Projet;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ProjetRepository extends JpaRepository<Projet,Long > {


    List<Projet> findByClientId(Long id);



    List<Projet> findByChefEquipeId(Long chefEquipeId);


    @Query("SELECT p FROM Projet p LEFT JOIN FETCH p.feedbacks")
    List<Projet> findAllWithFeedbacks();
}
