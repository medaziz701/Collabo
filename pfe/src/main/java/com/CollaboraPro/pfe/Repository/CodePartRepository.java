package com.CollaboraPro.pfe.Repository;

import com.CollaboraPro.pfe.Entity.CodePart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;

public interface CodePartRepository extends JpaRepository<CodePart, Long> {
    @Query("SELECT c FROM CodePart c WHERE c.tache.id = :tacheId")
    List<CodePart> findByTacheId(@Param("tacheId") Long tacheId);

    @Query("SELECT c FROM CodePart c WHERE c.projet.id = :projetId")
    List<CodePart> findByProjetId(@Param("projetId") Long projetId);

    void deleteByProjetId(Long id);
}