package com.CollaboraPro.pfe.Entity;


import com.fasterxml.jackson.annotation.JsonIgnore;
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
    private String content;
    private String author;

    @ManyToOne
    @JoinColumn(name = "tache_id")
    @JsonIgnore
    private Tache tache;

    @ManyToOne
    @JoinColumn(name = "projet_id")
    @JsonIgnore
    private Projet projet;

}