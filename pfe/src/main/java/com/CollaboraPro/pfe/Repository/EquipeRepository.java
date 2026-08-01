package com.CollaboraPro.pfe.Repository;

import com.CollaboraPro.pfe.Entity.Equipe;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface EquipeRepository extends JpaRepository<Equipe , Long> {

    List<Equipe> findByChefEquipe_Id(Long chefId);

    List<Equipe> findByMembresId(Long developpeurId);

    @Query("SELECT DISTINCT e FROM Equipe e LEFT JOIN FETCH e.chefEquipe")
    List<Equipe> findAllWithDetails();

    @Query("SELECT DISTINCT e FROM Equipe e LEFT JOIN FETCH e.chefEquipe WHERE e.id = :id")
    Optional<Equipe> findByIdWithDetails(Long id);

}
