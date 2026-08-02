package com.CollaboraPro.pfe.Entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import lombok.Data;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Entity
@Data
public class Feedback {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    private int noteEtoiles;
    @Column(columnDefinition = "TEXT")
    private String commentaire;


    private LocalDateTime dateCreation = LocalDateTime.now();



    @ManyToOne
    @JoinColumn(name = "projet_id")
    @JsonBackReference("feedback-projet")
    private Projet projet;

    @ManyToOne
    @JoinColumn(name = "client_id")
    @JsonBackReference("feedback-client")
    private Client client;


}