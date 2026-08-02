package com.CollaboraPro.pfe.Entity;


import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Data;
import lombok.ToString;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Entity
@Data
@ToString(exclude = {"membres", "projets", "feedbacks", "chefEquipe"})
public class Equipe {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)// La valeur est générée automatiquement par la base (auto-increment)
    private Long id;

    private String nomEquipe;
    private String description;
    private String domaineSpecialisation;

    private LocalDate dateCreation;
    private LocalDate dateDerniereModification;

    // Nouveaux champs persistants
    @Column(name = "nombre_membres")
    private Integer nombreMembres;

    @Column(name = "membres_info", columnDefinition = "TEXT")
    private String membresInfo; // Stocké comme JSON

    @ManyToOne
    @JoinColumn(name = "chef_equipe_id")
    private ChefEquipe chefEquipe;

    @OneToMany(mappedBy = "equipe")
    @JsonIgnore
    private List<Projet> projets;

    @ManyToMany
    @JoinTable(
            name = "equipe_membres",
            joinColumns = @JoinColumn(name = "equipe_id"),
            inverseJoinColumns = @JoinColumn(name = "developpeur_id")
    )
    private List<Developpeur> membres;


    @PrePersist
    @PreUpdate
    private void calculerChampsDerives() {//Méthode appelée automatiquement avant la sauvegarde (@PrePersist) ou la mise à jour (@PreUpdate)
        // Mise à jour automatique avant sauvegarde
        this.nombreMembres = membres != null ? membres.size() : 0;//pour prendre la taille de la liste sinon retourne null

        this.membresInfo = membres != null ? membres.stream()//flux (stream) des objets m
                .map(m -> String.format(
                        "{\"nom\":\"%s\",\"prenom\":\"%s\",\"specialite\":\"%s\"}",//Pour chaque m, il construit une chaîne JSON
                        m.getNom(), m.getPrenom(), m.getSpecialite()))
                .collect(Collectors.joining(",", "[", "]")) : "[]";// collect Prend toutes les chaînes produites par .map() et les assemble en une seule grande chaîne au format JSON, avec les virgule etc
    }
}

