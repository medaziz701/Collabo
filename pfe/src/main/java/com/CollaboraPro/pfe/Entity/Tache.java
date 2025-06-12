package com.CollaboraPro.pfe.Entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

@Entity
@Data
public class Tache {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String description;



    private LocalDate dateCreation;
    private String  dateLimite;

    @Enumerated(EnumType.STRING)
    private StatutTache statut;

    @OneToMany(mappedBy = "tache", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<CodePart> codeParts;

    @ManyToOne
    @JoinColumn(name = "projet_id")
    @JsonIgnoreProperties("taches")
    private Projet projet;



    @ManyToOne
    @JoinColumn(name = "developpeur_id")

    private Developpeur assigneA;

    public enum StatutTache {
        A_FAIRE, EN_COURS, TERMINEE
    }
}