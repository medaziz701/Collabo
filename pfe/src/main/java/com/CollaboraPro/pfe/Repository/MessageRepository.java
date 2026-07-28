package com.CollaboraPro.pfe.Repository;

import com.CollaboraPro.pfe.Entity.Message;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface MessageRepository extends JpaRepository<Message, Long> {
    List<Message> findByProjetIdOrderByDateEnvoiAsc(Long projetId);

    void deleteByProjetId(Long id);




}