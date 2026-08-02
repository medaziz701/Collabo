package com.CollaboraPro.pfe.Entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.UUID;

@Data
@Entity
@NoArgsConstructor//Génère un constructeur sans arguments
public class ConfirmationToken {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)// La valeur est générée automatiquement
    @Column(name="token_id")
    private Long tokenId;

    @Column(name="confirmation_token")
    private String confirmationToken;//Pour Stocke le token

    @Temporal(TemporalType.TIMESTAMP)//pour Stocke à la fois la date et l'heure
    private Date createdDate;

    @OneToOne(targetEntity = ChefEquipe.class, fetch = FetchType.LAZY,cascade = CascadeType.ALL,orphanRemoval = true)
    @JoinColumn(nullable = false, name = "chefEquipeID")// FetchType.LAZY pour chargé seulement quand nécessaire
    private ChefEquipe chefEquipe;//orphanRemoval = true Si le token est supprimé le développeur associé l'est aussi




    public ConfirmationToken(ChefEquipe chefEquipe) {
        this.chefEquipe = chefEquipe;
        createdDate = new Date();
        confirmationToken = UUID.randomUUID().toString();//Génère un token aléatoire avec UUID
    }


    public ConfirmationToken(Date createdDate, String confirmationToken, ChefEquipe chefEquipe) {//Utile pour la reconstruction d'un token existant
        this.createdDate = createdDate;//Permet de spécifier tous les champs manuellement
        this.confirmationToken = confirmationToken;
        this.chefEquipe = chefEquipe;
    }
}
