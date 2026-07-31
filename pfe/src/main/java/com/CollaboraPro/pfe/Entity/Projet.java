package com.CollaboraPro.pfe.Entity;

import com.fasterxml.jackson.annotation.*;
import jakarta.persistence.*;
import lombok.Data;
import lombok.ToString;

import java.time.LocalDate;
import java.util.Collection;
import java.util.List;

@Entity
@Data
@ToString(exclude = {"taches", "equipe", "feedbacks", "client", "chefEquipe", "codeParts", "messages"})
public class Projet {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String nom;
    private String  datedeb ;
    private String datefin ;
    private String description;
    @Enumerated(EnumType.STRING)
    private StatutProjet statut;



    public enum StatutProjet {
        EN_COURS, TERMINE, ANNULE
    }

    @OneToMany(mappedBy = "projet", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private List<Tache> taches;




    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinColumn(name = "equipe_id")
    @JsonIgnore
    private Equipe equipe;

    @OneToMany(mappedBy = "projet",cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private List<Feedback> feedbacks;


    @ManyToOne
    @JoinColumn(name = "client_id")
    @JsonIgnore
    @ToString.Exclude
    private Client client;


    @ManyToOne
    @JoinColumn(name = "chef_equipe_id")
    @JsonIgnore
    @ToString.Exclude
    private ChefEquipe chefEquipe;

    @OneToMany(mappedBy = "projet", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private List<CodePart> codeParts;

    @OneToMany(mappedBy = "projet", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private List<Message> messages;
}