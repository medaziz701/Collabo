package com.CollaboraPro.pfe.DTO;

import com.CollaboraPro.pfe.Entity.Tache;
import lombok.Data;

import java.util.List;

@Data
public class TacheDTO {
    private Long id;
    private String description;
    private String dateCreation;
    private String dateLimite;
    private String statut;
    private ProjectInfo projet;
    private AssignedDeveloperInfo assigneA;

    @Data
    public static class ProjectInfo {
        private Long id;
        private String nom;
        private String datedeb;
        private String datefin;
        private String description;
        private String statut;
        private Long equipeId;
        private String nomEquipe;
        private EquipeInfo equipe;
        private List<MembreInfo> membres;
        private ClientInfo client;
        private ChefEquipeInfo chefEquipe;
    }

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

    @Data
    public static class ClientInfo {
        private Long id;
        private String nom;
        private String prenom;
        private String email;
    }

    @Data
    public static class ChefEquipeInfo {
        private Long id;
        private String nom;
        private String prenom;
    }

    @Data
    public static class AssignedDeveloperInfo {
        private Long id;
        private String nom;
        private String prenom;
    }

    public static TacheDTO fromEntity(Tache tache) {
        TacheDTO dto = new TacheDTO();
        dto.setId(tache.getId());
        dto.setDescription(tache.getDescription());
        dto.setDateCreation(tache.getDateCreation() != null ? tache.getDateCreation().toString() : null);
        dto.setDateLimite(tache.getDateLimite());
        dto.setStatut(tache.getStatut() != null ? tache.getStatut().toString() : null);

        if (tache.getProjet() != null) {
            ProjectInfo projectInfo = new ProjectInfo();
            projectInfo.setId(tache.getProjet().getId());
            projectInfo.setNom(tache.getProjet().getNom());
            projectInfo.setDatedeb(tache.getProjet().getDatedeb());
            projectInfo.setDatefin(tache.getProjet().getDatefin());
            projectInfo.setDescription(tache.getProjet().getDescription());
            projectInfo.setStatut(tache.getProjet().getStatut() != null ? tache.getProjet().getStatut().toString() : null);

            if (tache.getProjet().getEquipe() != null) {
                projectInfo.setEquipeId(tache.getProjet().getEquipe().getId());
                projectInfo.setNomEquipe(tache.getProjet().getEquipe().getNomEquipe());

                EquipeInfo equipeInfo = new EquipeInfo();
                equipeInfo.setId(tache.getProjet().getEquipe().getId());
                equipeInfo.setNomEquipe(tache.getProjet().getEquipe().getNomEquipe());
                equipeInfo.setDescription(tache.getProjet().getEquipe().getDescription());
                equipeInfo.setDomaineSpecialisation(tache.getProjet().getEquipe().getDomaineSpecialisation());
                projectInfo.setEquipe(equipeInfo);

                if (tache.getProjet().getEquipe().getMembres() != null) {
                    List<MembreInfo> membresInfo = tache.getProjet().getEquipe().getMembres().stream()
                            .map(membre -> {
                                MembreInfo membreInfo = new MembreInfo();
                                membreInfo.setId(membre.getId());
                                membreInfo.setNom(membre.getNom());
                                membreInfo.setPrenom(membre.getPrenom());
                                return membreInfo;
                            })
                            .collect(java.util.stream.Collectors.toList());
                    projectInfo.setMembres(membresInfo);
                }
            }

            if (tache.getProjet().getClient() != null) {
                ClientInfo clientInfo = new ClientInfo();
                clientInfo.setId(tache.getProjet().getClient().getId());
                clientInfo.setNom(tache.getProjet().getClient().getNom());
                clientInfo.setPrenom(tache.getProjet().getClient().getPrenom());
                clientInfo.setEmail(tache.getProjet().getClient().getEmail());
                projectInfo.setClient(clientInfo);
            }

            if (tache.getProjet().getChefEquipe() != null) {
                ChefEquipeInfo chefEquipeInfo = new ChefEquipeInfo();
                chefEquipeInfo.setId(tache.getProjet().getChefEquipe().getId());
                chefEquipeInfo.setNom(tache.getProjet().getChefEquipe().getNom());
                chefEquipeInfo.setPrenom(tache.getProjet().getChefEquipe().getPrenom());
                projectInfo.setChefEquipe(chefEquipeInfo);
            }

            dto.setProjet(projectInfo);
        }

        if (tache.getAssigneA() != null) {
            AssignedDeveloperInfo devInfo = new AssignedDeveloperInfo();
            devInfo.setId(tache.getAssigneA().getId());
            devInfo.setNom(tache.getAssigneA().getNom());
            devInfo.setPrenom(tache.getAssigneA().getPrenom());
            dto.setAssigneA(devInfo);
        }

        return dto;
    }
}
