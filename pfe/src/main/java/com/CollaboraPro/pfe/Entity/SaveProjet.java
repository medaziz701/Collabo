package com.CollaboraPro.pfe.Entity;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

import jakarta.persistence.Column;
import jakarta.persistence.Lob;
import lombok.Data;

@Data
public class SaveProjet {
    private Long id;
    private String nom;
    private LocalDate datedeb;
    private LocalDate datefin;
    private String description;
    private Projet.StatutProjet statut;
    @Lob
    @Column(columnDefinition = "LONGTEXT")
    private Long idClient;
    private List<Long> developpeursIds;
    private String nomEquipe;
    private String descriptionEquipe;
    private String domaineSpecialisation;
    private Map<Long, TacheInfo> tachesDeveloppeurs;
    private Long idChefEquipe;

    @Data
    public static class TacheInfo {
        private String description;
        private String dateLimite;
    }

    public static Projet toEntity(SaveProjet model) {
        if(model == null) {
            return null;
        }
        Projet projet = new Projet();
        projet.setId(model.getId());
        projet.setNom(model.getNom());
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        projet.setDatedeb(model.getDatedeb().format(formatter));
        projet.setDatefin(model.getDatefin().format(formatter));
        projet.setDescription(model.getDescription());
        projet.setStatut(model.getStatut());
        return projet;
    }
}