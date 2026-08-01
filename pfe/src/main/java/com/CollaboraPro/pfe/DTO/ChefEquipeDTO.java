package com.CollaboraPro.pfe.DTO;

import com.CollaboraPro.pfe.Entity.ChefEquipe;
import lombok.Data;

@Data
public class ChefEquipeDTO {
    private Long id;
    private String nom;
    private String prenom;
    private String tlf;
    private String email;
    private String adresse;
    private String genre;
    private String cin;
    private String dateNaissance;
    private String datePriseFonction;
    private boolean etat;

    public static ChefEquipeDTO fromEntity(ChefEquipe chefEquipe) {
        if (chefEquipe == null) {
            return null;
        }
        ChefEquipeDTO dto = new ChefEquipeDTO();
        dto.setId(chefEquipe.getId());
        dto.setNom(chefEquipe.getNom());
        dto.setPrenom(chefEquipe.getPrenom());
        dto.setTlf(chefEquipe.getTlf());
        dto.setEmail(chefEquipe.getEmail());
        dto.setAdresse(chefEquipe.getAdresse());
        dto.setGenre(chefEquipe.getGenre());
        dto.setCin(chefEquipe.getCin());
        dto.setDateNaissance(chefEquipe.getDateNaissance());
        dto.setDatePriseFonction(chefEquipe.getDatePriseFonction());
        dto.setEtat(chefEquipe.isEtat());
        return dto;
    }
}
