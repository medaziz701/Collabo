package com.CollaboraPro.pfe.Entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Data;
import lombok.ToString;

import java.time.LocalDate;
import java.util.List;

@Entity
@Data
@ToString(exclude = {"managedByAdmin", "projets", "feedbacks"})
public class Client {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String nom;
    private String prenom;
    private String tlf;
    private String email;
    private String cin;
    private String mp;
    private String adresse;
    private String genre;
    private boolean etat;



    @OneToMany(mappedBy = "client")
    @JsonIgnore
    private List<Projet> projets;


    @ManyToOne
    @JoinColumn(name = "admin_id")
    @JsonIgnore
    private Admin managedByAdmin;



    @OneToMany(mappedBy = "client")
    @JsonManagedReference("feedback-client") //matcher le nom dans Feedback
    private List<Feedback> feedbacks;
}