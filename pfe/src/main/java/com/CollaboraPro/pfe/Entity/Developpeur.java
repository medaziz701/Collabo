package com.CollaboraPro.pfe.Entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Data;
import lombok.ToString;

import java.util.List;

@Entity
@Data
@ToString(exclude = {"equipes", "tachesAssignees", "documentsUploades"})
public class Developpeur {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String nom;
    private String prenom;
    private String tlf;
    private String cin;
    private String email;
    private String mp;
    private String adresse;
    private String specialite;
    private boolean etat;
    private String genre;
    private boolean disponibilite = true;

    @ManyToMany(mappedBy = "membres")
    @JsonIgnoreProperties("membres")//en evite membres (developpeur) dans l'affichage equipe
    private List<Equipe> equipes;



    @OneToMany(mappedBy = "assigneA")
    @JsonIgnore
    private List<Tache> tachesAssignees;




    @ManyToOne
    @JoinColumn(name = "admin_id")
    @JsonIgnore
    private Admin managedByAdmin;


}