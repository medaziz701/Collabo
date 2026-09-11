package com.CollaboraPro.pfe.Repository;

import com.CollaboraPro.pfe.Entity.CodePart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.util.List;

public interface CodePartRepository extends JpaRepository<CodePart, Long> {
    @Query("SELECT cp FROM CodePart cp LEFT JOIN FETCH cp.tache LEFT JOIN FETCH cp.projet WHERE cp.tache.id = :tacheId")
    List<CodePart> findByTacheIdWithRelations(Long tacheId);

    @Query("SELECT cp FROM CodePart cp LEFT JOIN FETCH cp.tache LEFT JOIN FETCH cp.projet WHERE cp.projet.id = :projetId")
    List<CodePart> findByProjetIdWithRelations(Long projetId);

    List<CodePart> findByTacheId(Long tacheId);

    List<CodePart> findByProjetId(Long projetId);

    void deleteByProjetId(Long id);
}