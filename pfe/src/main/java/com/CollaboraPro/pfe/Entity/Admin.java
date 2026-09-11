package com.CollaboraPro.pfe.Entity;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;
import lombok.ToString;

import java.util.List;

@Entity
@Data
@ToString(exclude = {"clientsGeres", "chefsCrees", "developpeursGeres", "contactsAssocies"})
public class Admin {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String nom;
    private String prenom;
    private String tlf;
    private String email;
    private String mp;

    private String genre;


    @OneToMany(mappedBy = "createdByAdmin")
    @JsonIgnore
    private List<ChefEquipe> chefsCrees;



    @OneToMany(mappedBy = "managedByAdmin")
    @JsonIgnore
    private List<Client> clientsGeres;

    @OneToMany(mappedBy = "managedByAdmin")
    @JsonIgnore
    private List<Developpeur> developpeursGeres;



    @OneToMany(mappedBy = "managedByAdmin")
    @JsonIgnore
    private List<Contact> contactsAssocies;
}

