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
    private List<Tache> taches;





    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinColumn(name = "equipe_id")
    private Equipe equipe;

    @OneToMany(mappedBy = "projet",cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference("feedback-projet")
    private List<Feedback> feedbacks;


    @ManyToOne
    @JoinColumn(name = "client_id")

    @ToString.Exclude
    private Client client;


    @ManyToOne
    @JoinColumn(name = "chef_equipe_id")
    @JsonBackReference("projet-chef-reference")
    @ToString.Exclude
    private ChefEquipe chefEquipe;

    @OneToMany(mappedBy = "projet", cascade = CascadeType.ALL, orphanRemoval = true)

    private List<CodePart> codeParts;

    @OneToMany(mappedBy = "projet", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnoreProperties("projet")
    private List<Message> messages;
}