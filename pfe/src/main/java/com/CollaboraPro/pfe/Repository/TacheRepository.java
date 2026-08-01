package com.CollaboraPro.pfe.Repository;

import com.CollaboraPro.pfe.Entity.Developpeur;
import com.CollaboraPro.pfe.Entity.Projet;
import com.CollaboraPro.pfe.Entity.Tache;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface TacheRepository  extends JpaRepository<Tache, Long> {
    List<Tache> findByProjetId(Long id);




    @Modifying
    void deleteByProjetIdAndAssigneAIdNotIn(Long projetId, List<Long> devIds);

    Optional<Tache> findByProjetIdAndAssigneAId(Long projetId, Long devId);

    void deleteByProjetId(Long id);

    @Modifying
    @Query("DELETE FROM Tache t WHERE t.projet.id = :projetId AND t.assigneA.id = :developpeurId")
    void deleteByProjetIdAndAssigneAId(@Param("projetId") Long projetId,
                                       @Param("developpeurId") Long developpeurId);

    @Query("SELECT DISTINCT t FROM Tache t LEFT JOIN FETCH t.projet p LEFT JOIN FETCH p.equipe e LEFT JOIN FETCH e.membres LEFT JOIN FETCH p.client LEFT JOIN FETCH p.chefEquipe LEFT JOIN FETCH t.assigneA")
    List<Tache> findAllWithDetails();

    @Query("SELECT DISTINCT t FROM Tache t LEFT JOIN FETCH t.projet p LEFT JOIN FETCH p.equipe e LEFT JOIN FETCH e.membres LEFT JOIN FETCH p.client LEFT JOIN FETCH p.chefEquipe LEFT JOIN FETCH t.assigneA WHERE t.id = :id")
    Optional<Tache> findByIdWithDetails(@Param("id") Long id);

}
