package com.CollaboraPro.pfe.DTO;

import com.CollaboraPro.pfe.Entity.Client;
import lombok.Data;

@Data
public class ClientDTO {
    private Long id;
    private String nom;
    private String prenom;
    private String tlf;
    private String email;
    private String cin;
    private String adresse;
    private String genre;
    private boolean etat;

    public static ClientDTO fromEntity(Client client) {
        if (client == null) {
            return null;
        }
        ClientDTO dto = new ClientDTO();
        dto.setId(client.getId());
        dto.setNom(client.getNom());
        dto.setPrenom(client.getPrenom());
        dto.setTlf(client.getTlf());
        dto.setEmail(client.getEmail());
        dto.setCin(client.getCin());
        dto.setAdresse(client.getAdresse());
        dto.setGenre(client.getGenre());
        dto.setEtat(client.isEtat());
        return dto;
    }
}
