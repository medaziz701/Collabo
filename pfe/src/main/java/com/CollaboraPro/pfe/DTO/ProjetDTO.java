package com.CollaboraPro.pfe.DTO;

import com.CollaboraPro.pfe.Entity.Projet;
import lombok.Data;

@Data
public class ProjetDTO {
    private Long id;
    private String nom;
    private String datedeb;
    private String datefin;
    private String description;
    private String statut;
    private Long equipeId;
    private String nomEquipe;
    private Long clientId;
    private String clientNom;
    private Long chefEquipeId;
    private String chefEquipeNom;

    public static ProjetDTO fromEntity(Projet projet) {
        ProjetDTO dto = new ProjetDTO();
        dto.setId(projet.getId());
        dto.setNom(projet.getNom());
        dto.setDatedeb(projet.getDatedeb());
        dto.setDatefin(projet.getDatefin());
        dto.setDescription(projet.getDescription());
        dto.setStatut(projet.getStatut() != null ? projet.getStatut().toString() : null);

        if (projet.getEquipe() != null) {
            dto.setEquipeId(projet.getEquipe().getId());
            dto.setNomEquipe(projet.getEquipe().getNomEquipe());
        }

        if (projet.getClient() != null) {
            dto.setClientId(projet.getClient().getId());
            dto.setClientNom(projet.getClient().getNom() + " " + projet.getClient().getPrenom());
        }

        if (projet.getChefEquipe() != null) {
            dto.setChefEquipeId(projet.getChefEquipe().getId());
            dto.setChefEquipeNom(projet.getChefEquipe().getNom() + " " + projet.getChefEquipe().getPrenom());
        }

        return dto;
    }
}
