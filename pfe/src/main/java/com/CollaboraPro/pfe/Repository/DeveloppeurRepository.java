package com.CollaboraPro.pfe.Repository;

import com.CollaboraPro.pfe.Entity.Developpeur;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface DeveloppeurRepository extends JpaRepository<Developpeur, Long> {
    boolean existsByEmail(String email);
    Developpeur findDeveloppeurByEmail(String email);

    @Query("SELECT d FROM Developpeur d WHERE d.disponibilite = ?1 AND d.etat = true")
    List<Developpeur> findByDisponibilite(boolean disponibilite);




    @Query("SELECT d FROM Developpeur d JOIN d.equipes e WHERE e.id = :equipeId")
    List<Developpeur> findByEquipeId(@Param("equipeId") Long equipeId);
}