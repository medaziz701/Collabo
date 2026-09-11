package com.CollaboraPro.pfe.Repository;

import com.CollaboraPro.pfe.Entity.Contact;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ContactRepository  extends JpaRepository<Contact, Long> {
}
