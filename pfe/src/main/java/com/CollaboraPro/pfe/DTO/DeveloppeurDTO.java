package com.CollaboraPro.pfe.DTO;

import com.CollaboraPro.pfe.Entity.Developpeur;
import lombok.Data;

@Data
public class DeveloppeurDTO {
    private Long id;
    private String nom;
    private String prenom;
    private String tlf;
    private String cin;
    private String email;
    private String adresse;
    private String specialite;
    private boolean etat;
    private String genre;
    private boolean disponibilite;

    public static DeveloppeurDTO fromEntity(Developpeur developpeur) {
        if (developpeur == null) {
            return null;
        }
        DeveloppeurDTO dto = new DeveloppeurDTO();
        dto.setId(developpeur.getId());
        dto.setNom(developpeur.getNom());
        dto.setPrenom(developpeur.getPrenom());
        dto.setTlf(developpeur.getTlf());
        dto.setCin(developpeur.getCin());
        dto.setEmail(developpeur.getEmail());
        dto.setAdresse(developpeur.getAdresse());
        dto.setSpecialite(developpeur.getSpecialite());
        dto.setEtat(developpeur.isEtat());
        dto.setGenre(developpeur.getGenre());
        dto.setDisponibilite(developpeur.isDisponibilite());
        return dto;
    }
}
