package com.CollaboraPro.pfe.Entity;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Entity
@Data
public class Notification {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Enumerated(EnumType.STRING)
    private NotificationType titre;
    private String contenu;

    private LocalDateTime dateCreation = LocalDateTime.now();
    private boolean estLue = false;

    @ManyToOne
    @JoinColumn(name = "admin_id")
    private Admin admin;





}

