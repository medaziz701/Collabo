package com.CollaboraPro.pfe.Entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@Entity
public class Commentaire {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String contenu;
    private String auteur;
    private LocalDateTime dateCreation;

    @ManyToOne
    @JoinColumn(name = "code_part_id")
    @JsonIgnore
    private CodePart codePart;

    @ManyToOne
    @JoinColumn(name = "chef_equipe_id")
    @JsonIgnore
    private ChefEquipe chefEquipe;


}