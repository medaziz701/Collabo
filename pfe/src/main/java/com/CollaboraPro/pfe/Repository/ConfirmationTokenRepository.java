package com.CollaboraPro.pfe.Repository;

import com.CollaboraPro.pfe.Entity.ConfirmationToken;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ConfirmationTokenRepository extends JpaRepository <ConfirmationToken, Long> {

    ConfirmationToken findByConfirmationToken(String confirmationToken);
}
