package com.CollaboraPro.pfe.Repository;

import com.CollaboraPro.pfe.Entity.Feedback;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FeedbackRepository extends JpaRepository<Feedback, Long> {
    List<Feedback> findByProjetId(Long projetId);
    List<Feedback> findByClientId(Long clientId);

    void deleteByProjetId(Long id);
}
