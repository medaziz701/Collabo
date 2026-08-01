package com.CollaboraPro.pfe.DTO;

import com.CollaboraPro.pfe.Entity.Projet;
import lombok.Data;

import java.util.List;

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
    private EquipeInfo equipe;
    private List<MembreInfo> membres;

    @Data
    public static class EquipeInfo {
        private Long id;
        private String nomEquipe;
        private String description;
        private String domaineSpecialisation;
    }

    @Data
    public static class MembreInfo {
        private Long id;
        private String nom;
        private String prenom;
    }

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

            // Populate equipe info
            EquipeInfo equipeInfo = new EquipeInfo();
            equipeInfo.setId(projet.getEquipe().getId());
            equipeInfo.setNomEquipe(projet.getEquipe().getNomEquipe());
            equipeInfo.setDescription(projet.getEquipe().getDescription());
            equipeInfo.setDomaineSpecialisation(projet.getEquipe().getDomaineSpecialisation());
            dto.setEquipe(equipeInfo);

            // Populate membres info
            if (projet.getEquipe().getMembres() != null) {
                List<MembreInfo> membresInfo = projet.getEquipe().getMembres().stream()
                        .map(membre -> {
                            MembreInfo membreInfo = new MembreInfo();
                            membreInfo.setId(membre.getId());
                            membreInfo.setNom(membre.getNom());
                            membreInfo.setPrenom(membre.getPrenom());
                            return membreInfo;
                        })
                        .collect(java.util.stream.Collectors.toList());
                dto.setMembres(membresInfo);
            }
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
