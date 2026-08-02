package com.CollaboraPro.pfe.Entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class Contact {
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id;
        private String nom;
        private String prenom;
        private String sujet;
        private String msg;
        private String tlf;
        private String email;

        @ManyToOne
        @JoinColumn(name = "admin_id")
        @JsonIgnore
        private Admin managedByAdmin;





}
