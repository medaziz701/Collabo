package com.CollaboraPro.pfe.Entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Data;
import lombok.ToString;

import java.time.LocalDate;
import java.util.List;

@Entity
@Data
@ToString(exclude = {"equipesDirigees", "createdByAdmin", "projet"})//Cela signifie que ces champs ne seront pas inclus dans la méthode toString() générée.  our éviter des boucles infinies lors de l'appel à toString()
//relation inverse vers ChefEquipe avec admin, alors appeler toString() peut entraîner une boucle infinie
public class ChefEquipe {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String nom;
    private String prenom;
    private String tlf;
    private String email;
    private  String adresse;
    private String mp;
    private String genre;
    private String cin;

    private String dateNaissance;

    private String datePriseFonction;
    private boolean etat;





    @OneToMany(mappedBy = "chefEquipe")
    @JsonIgnore
    private List<Equipe> equipesDirigees;

    @ManyToOne
    @JoinColumn(name = "admin_id")
    @JsonIgnore
    private Admin createdByAdmin;




}

