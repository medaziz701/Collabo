package com.CollaboraPro.pfe.Entity;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
public class CodePart {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String filename;
    @Lob
    @JsonIgnore
    private String content;
    private String author;

    @ManyToOne
    @JoinColumn(name = "tache_id")
    @JsonIgnoreProperties("codeParts")
    private Tache tache;

    @ManyToOne
    @JoinColumn(name = "projet_id")
    @JsonIgnoreProperties({"codeParts", "taches"})
    private Projet projet;

}