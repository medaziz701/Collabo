package com.CollaboraPro.pfe.Services;

import com.CollaboraPro.pfe.Entity.*;
import com.CollaboraPro.pfe.Repository.*;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class ProjetServiceImpl implements ProjetService {

    @Autowired
    private EquipeRepository equipeRepository;

    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    private ProjetRepository projetRepository;

    @Autowired
    private DeveloppeurRepository developpeurRepository;

    @Autowired
    private TacheRepository tacheRepository;
    @Autowired
    private ChefEquipeRepository chefEquipeRepository;

    @Autowired
    private FeedbackRepository feedbackRepository;

    @Autowired
    private MessageRepository messageRepository;

    @Autowired
    private CodePartRepository codePartRepository;

    @Override
    public Projet ajouterProjet(SaveProjet model) {
        // Création basique du projet
        Projet projet = SaveProjet.toEntity(model);

        // Assignation du client
        Client client = clientRepository.findById(model.getIdClient()).orElseThrow();
        projet.setClient(client);

        if(model.getIdChefEquipe() != null) {
            ChefEquipe chefEquipe = chefEquipeRepository.findById(model.getIdChefEquipe())
                    .orElseThrow(() -> new RuntimeException("Chef d'équipe non trouvé"));
            projet.setChefEquipe(chefEquipe);
        }

        // Création de l'équipe
        Equipe equipe = new Equipe();
        equipe.setNomEquipe(model.getNomEquipe());
        equipe.setDescription(model.getDescriptionEquipe());
        equipe.setDomaineSpecialisation(model.getDomaineSpecialisation());
        equipe.setDateCreation(LocalDate.now());
        equipe.setDateDerniereModification(LocalDate.now());
        if(model.getIdChefEquipe() != null) {
            ChefEquipe chefEquipe = chefEquipeRepository.findById(model.getIdChefEquipe())
                    .orElseThrow(() -> new RuntimeException("Chef d'équipe non trouvé"));
            equipe.setChefEquipe(chefEquipe);
        }

        // Assignation des membres
        List<Developpeur> membres = developpeurRepository.findAllById(model.getDeveloppeursIds());
        equipe.setMembres(membres);
        equipe = equipeRepository.save(equipe);

        // Assignation de l'équipe et sauvegarde
        projet.setEquipe(equipe);
        Projet savedProjet = projetRepository.save(projet);

        // Création des tâches
        if(model.getTachesDeveloppeurs() != null && !model.getTachesDeveloppeurs().isEmpty()) {
            model.getTachesDeveloppeurs().forEach((devId, tacheInfo) -> {
                Tache tache = new Tache();
                tache.setDescription(tacheInfo.getDescription());
                tache.setDateCreation(LocalDate.now());
                tache.setDateLimite(tacheInfo.getDateLimite()); // Stockage direct de la date
                tache.setStatut(Tache.StatutTache.A_FAIRE);
                tache.setProjet(savedProjet);

                Developpeur dev = developpeurRepository.findById(devId)
                        .orElseThrow(() -> new RuntimeException("Développeur non trouvé"));
                tache.setAssigneA(dev);

                tacheRepository.save(tache);
            });
        }

        return savedProjet;
    }

    @Override
    public List<Projet> afficherProjet() {
        return projetRepository.findAll();
    }

    @Override
    public List<Projet> getProjetByClient(Long id) {
        return projetRepository.findByClientId(id);
    }


    @Override
    public List<Projet> getProjetByChefEquipe(Long id) {

        return projetRepository.findByChefEquipeId(id);
    }

    @Override
    @Transactional
    public Projet modifierProjet(Long id, SaveProjet model) {
        Projet projetExist = projetRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Projet non trouvé"));

        // Mise à jour des infos de base
        projetExist.setNom(model.getNom());
        projetExist.setDatedeb(model.getDatedeb().format(DateTimeFormatter.ISO_DATE));
        projetExist.setDatefin(model.getDatefin().format(DateTimeFormatter.ISO_DATE));
        projetExist.setDescription(model.getDescription());
        projetExist.setStatut(model.getStatut());

        // Gestion de l'équipe
        Equipe equipe = projetExist.getEquipe();
        if (equipe != null) {
            // Mettre à jour les infos de l'équipe
            equipe.setNomEquipe(model.getNomEquipe());
            equipe.setDescription(model.getDescriptionEquipe());
            equipe.setDomaineSpecialisation(model.getDomaineSpecialisation());
            equipe.setDateDerniereModification(LocalDate.now());

            // Gestion des membres
            List<Developpeur> anciensMembres = new ArrayList<>(equipe.getMembres());
            List<Developpeur> nouveauxMembres = developpeurRepository.findAllById(model.getDeveloppeursIds());

            // Trouver les membres à retirer
            List<Developpeur> membresARetirer = anciensMembres.stream()
                    .filter(m -> !nouveauxMembres.contains(m))
                    .collect(Collectors.toList());

            // Mettre à jour la disponibilité des membres retirés
            membresARetirer.forEach(m -> {
                m.setDisponibilite(true);
                developpeurRepository.save(m);

                // 2. Supprimer les tâches associées
                tacheRepository.deleteByProjetIdAndAssigneAId(id, m.getId());
            });

            // Mettre à jour la disponibilité des nouveaux membres
            nouveauxMembres.forEach(m -> {
                if (!anciensMembres.contains(m)) {
                    m.setDisponibilite(false);
                    developpeurRepository.save(m);
                }
            });

            equipe.setMembres(nouveauxMembres);
            equipeRepository.save(equipe);
        }

        // Gestion des tâches
        if (model.getTachesDeveloppeurs() != null) {
            model.getTachesDeveloppeurs().forEach((devId, tacheInfo) -> {
                // Vérifier si la tâche existe déjà
                Optional<Tache> tacheExistante = tacheRepository.findByProjetIdAndAssigneAId(id, devId);

                if (tacheExistante.isPresent()) {
                    // Mettre à jour la tâche existante
                    Tache tache = tacheExistante.get();
                    tache.setDescription(tacheInfo.getDescription());
                    tache.setDateLimite(tacheInfo.getDateLimite());
                    tacheRepository.save(tache);
                } else {
                    // Créer une nouvelle tâche
                    Tache nouvelleTache = new Tache();
                    nouvelleTache.setDescription(tacheInfo.getDescription());
                    nouvelleTache.setDateCreation(LocalDate.now());
                    nouvelleTache.setDateLimite(tacheInfo.getDateLimite());
                    nouvelleTache.setStatut(Tache.StatutTache.A_FAIRE);
                    nouvelleTache.setProjet(projetExist);

                    Developpeur dev = developpeurRepository.findById(devId)
                            .orElseThrow(() -> new RuntimeException("Développeur non trouvé"));
                    nouvelleTache.setAssigneA(dev);

                    tacheRepository.save(nouvelleTache);
                }
            });
        }

        return projetRepository.save(projetExist);
    }


    public void updateProjetStatut(Long projetId) {
        List<Tache> taches = tacheRepository.findByProjetId(projetId);
        boolean toutesTerminees = !taches.isEmpty() && taches.stream()
                .allMatch(t -> t.getStatut() == Tache.StatutTache.TERMINEE);

        if (toutesTerminees) {
            Projet projet = projetRepository.findById(projetId)
                    .orElseThrow(() -> new RuntimeException("Projet non trouvé"));
            projet.setStatut(Projet.StatutProjet.TERMINE);
            projetRepository.save(projet);
        }
    }
    @Override
    public Projet updateProjetStatut(Long projetId, String nouveauStatut) {
        Projet projet = projetRepository.findById(projetId)
                .orElseThrow(() -> new RuntimeException("Projet non trouvé"));

        try {
            Projet.StatutProjet statut = Projet.StatutProjet.valueOf(nouveauStatut);
            projet.setStatut(statut);
            return projetRepository.save(projet);
        } catch (IllegalArgumentException e) {
            throw new RuntimeException("Statut invalide: " + nouveauStatut);
        }
    }

    @Override
    public Optional<Projet> afficherProjetById(Long id) {
        return projetRepository.findById(id);
    }


    @Override
    @Transactional
    public void supprimerProjet(Long id) {
        Projet projet = projetRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Projet non trouvé"));

        // 1. Supprimer les messages associés (si vous avez une table message)
        messageRepository.deleteByProjetId(id);

        // 2. Supprimer les feedbacks
        feedbackRepository.deleteByProjetId(id);

        // 3. Supprimer les code parts
        codePartRepository.deleteByProjetId(id);

        // 4. Supprimer les tâches
        tacheRepository.deleteByProjetId(id);

        // 5. Supprimer l'équipe associée
        if (projet.getEquipe() != null) {
            equipeRepository.delete(projet.getEquipe());
        }

        // 6. Enfin supprimer le projet
        projetRepository.delete(projet);
    }



    @Override
    public Tache modifierTacheProjet(Long projetId, Long tacheId, Tache tacheModifiee) {
        Projet projet = projetRepository.findById(projetId)
                .orElseThrow(() -> new RuntimeException("Projet non trouvé"));

        Tache tache = tacheRepository.findById(tacheId)
                .orElseThrow(() -> new RuntimeException("Tâche non trouvée"));

        if (!tache.getProjet().getId().equals(projet.getId())) {
            throw new RuntimeException("La tâche n'appartient pas à ce projet");
        }

        tache.setDescription(tacheModifiee.getDescription());
        tache.setDateLimite(tacheModifiee.getDateLimite());
        tache.setStatut(tacheModifiee.getStatut());

        if (tacheModifiee.getAssigneA() != null) {
            Developpeur dev = developpeurRepository.findById(tacheModifiee.getAssigneA().getId())
                    .orElseThrow(() -> new RuntimeException("Développeur non trouvé"));
            tache.setAssigneA(dev);
        }

        return tacheRepository.save(tache);
    }

    @Override
    public Tache ajouterTacheAuProjet(Long projetId, Tache nouvelleTache) {
        Projet projet = projetRepository.findById(projetId)
                .orElseThrow(() -> new RuntimeException("Projet non trouvé"));

        nouvelleTache.setProjet(projet);
        nouvelleTache.setDateCreation(LocalDate.now());
        nouvelleTache.setStatut(Tache.StatutTache.A_FAIRE);

        if (nouvelleTache.getAssigneA() != null) {
            Developpeur dev = developpeurRepository.findById(nouvelleTache.getAssigneA().getId())
                    .orElseThrow(() -> new RuntimeException("Développeur non trouvé"));
            nouvelleTache.setAssigneA(dev);
        }

        return tacheRepository.save(nouvelleTache);
    }


    @Transactional
    public Equipe creerEquipe(String nomEquipe, String description, String domaineSpecialisation,
                              Long chefEquipeId, List<Long> developpeursIds) {
        // Création de l'équipe
        Equipe equipe = new Equipe();
        equipe.setNomEquipe(nomEquipe);
        equipe.setDescription(description);
        equipe.setDomaineSpecialisation(domaineSpecialisation);
        equipe.setDateCreation(LocalDate.now());
        equipe.setDateDerniereModification(LocalDate.now());

        // Assignation du chef d'équipe si spécifié
        if(chefEquipeId != null) {
            ChefEquipe chefEquipe = chefEquipeRepository.findById(chefEquipeId)
                    .orElseThrow(() -> new RuntimeException("Chef d'équipe non trouvé"));
            equipe.setChefEquipe(chefEquipe);
        }

        // Assignation des membres
        List<Developpeur> membres = developpeurRepository.findAllById(developpeursIds);

        // Mise à jour de la disponibilité des membres
        membres.forEach(m -> {
            m.setDisponibilite(false);
            developpeurRepository.save(m);
        });

        equipe.setMembres(membres);

        return equipeRepository.save(equipe);
    }

    @Transactional
    public Equipe ajouterMembreAEquipe(Long equipeId, Long developpeurId) {
        Equipe equipe = equipeRepository.findById(equipeId)
                .orElseThrow(() -> new RuntimeException("Équipe non trouvée"));

        Developpeur dev = developpeurRepository.findById(developpeurId)
                .orElseThrow(() -> new RuntimeException("Développeur non trouvé"));

        if(!equipe.getMembres().contains(dev)) {
            dev.setDisponibilite(false);
            developpeurRepository.save(dev);

            equipe.getMembres().add(dev);
            equipe.setDateDerniereModification(LocalDate.now());
        }

        return equipeRepository.save(equipe);
    }

    @Transactional
    public Equipe retirerMembreDeEquipe(Long equipeId, Long developpeurId) {
        Equipe equipe = equipeRepository.findById(equipeId)
                .orElseThrow(() -> new RuntimeException("Équipe non trouvée"));

        Developpeur dev = developpeurRepository.findById(developpeurId)
                .orElseThrow(() -> new RuntimeException("Développeur non trouvé"));

        if(equipe.getMembres().contains(dev)) {
            // 1. Supprimer les tâches associées à ce membre dans tous les projets de l'équipe
            equipe.getProjets().forEach(projet -> {
                tacheRepository.deleteByProjetIdAndAssigneAId(projet.getId(), developpeurId);
            });

            // 2. Retirer le membre
            equipe.getMembres().remove(dev);
            dev.setDisponibilite(true);

            // 3. Sauvegarder
            developpeurRepository.save(dev);
            equipe.setDateDerniereModification(LocalDate.now());
        }

        return equipeRepository.save(equipe);
    }


}