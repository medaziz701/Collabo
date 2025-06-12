package com.CollaboraPro.pfe.Repository;

import com.CollaboraPro.pfe.Entity.CodePart;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface CodePartRepository extends JpaRepository<CodePart, Long> {
    List<CodePart> findByTacheId(Long tacheId);

    List<CodePart> findByProjetId(Long projetId);

    void deleteByProjetId(Long id);
}