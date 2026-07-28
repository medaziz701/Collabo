package com.CollaboraPro.pfe.Repository;

import com.CollaboraPro.pfe.Entity.Client;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClientRepository extends JpaRepository <Client , Long> {
    boolean existsByEmail(String email);
    Client findClientByEmail(String email);
}
