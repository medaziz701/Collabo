package com.CollaboraPro.pfe.DTO;

import com.CollaboraPro.pfe.Entity.Equipe;
import lombok.Data;

import java.time.LocalDate;

@Data
public class EquipeDTO {
    private Long id;
    private String nomEquipe;
    private String description;
    private String domaineSpecialisation;
    private LocalDate dateCreation;
    private LocalDate dateDerniereModification;
    private Integer nombreMembres;
    private String membresInfo;
    private Long chefEquipeId;
    private String chefEquipeNom;
    private String chefEquipePrenom;

    public static EquipeDTO fromEntity(Equipe equipe) {
        if (equipe == null) {
            return null;
        }
        EquipeDTO dto = new EquipeDTO();
        dto.setId(equipe.getId());
        dto.setNomEquipe(equipe.getNomEquipe());
        dto.setDescription(equipe.getDescription());
        dto.setDomaineSpecialisation(equipe.getDomaineSpecialisation());
        dto.setDateCreation(equipe.getDateCreation());
        dto.setDateDerniereModification(equipe.getDateDerniereModification());
        dto.setNombreMembres(equipe.getNombreMembres());
        dto.setMembresInfo(equipe.getMembresInfo());

        if (equipe.getChefEquipe() != null) {
            dto.setChefEquipeId(equipe.getChefEquipe().getId());
            dto.setChefEquipeNom(equipe.getChefEquipe().getNom());
            dto.setChefEquipePrenom(equipe.getChefEquipe().getPrenom());
        }

        return dto;
    }
}
