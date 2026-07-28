package com.CollaboraPro.pfe.RestController;

import com.CollaboraPro.pfe.Entity.*;
import com.CollaboraPro.pfe.Repository.DeveloppeurRepository;
import com.CollaboraPro.pfe.Repository.EquipeRepository;
import com.CollaboraPro.pfe.Repository.ProjetRepository;
import com.CollaboraPro.pfe.Repository.TacheRepository;
import com.CollaboraPro.pfe.Services.NotificationService;
import com.CollaboraPro.pfe.Services.ProjetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@RestController
@CrossOrigin("*")
@RequestMapping(value = "/projet")
public class ProjetRestController {


    @Autowired
    private ProjetService projetService;

    @Autowired
    ProjetRepository projetRepository;

    @Autowired
    TacheRepository tacheRepository;
    @Autowired
    EquipeRepository equipeRepository;

    @Autowired
    DeveloppeurRepository developpeurRepository;

    @Autowired
    NotificationService notificationService;

    @PostMapping
    public Projet ajouterProjet(@RequestBody SaveProjet model) {
        return projetService.ajouterProjet(model);
    }

    @GetMapping
    public List<Projet> afficherProjet() {// pour récupérer tous les projets
        return projetService.afficherProjet();
    }

    @GetMapping("/client/{id}")
    public List<Projet> getProjetsByClient(@PathVariable Long id) {//pour récupérer les projets d'un client spécifique
        return projetService.getProjetByClient(id);
    }

    @GetMapping("/{id}/equipe")
    public ResponseEntity<?> getEquipeByProjet(@PathVariable Long id) {
        Optional<Projet> projet = projetRepository.findById(id);
        if (projet.isPresent() && projet.get().getEquipe() != null) {
            Equipe equipe = projet.get().getEquipe();

            // Récupérer les membres actuels depuis la base
            List<Developpeur> membres = developpeurRepository.findByEquipeId(equipe.getId());

            HashMap<String, Object> response = new HashMap<>();
            response.put("id", equipe.getId());
            response.put("nom", equipe.getNomEquipe());
            response.put("nombreMembres", equipe.getNombreMembres());
            response.put("membres", membres); // Envoyer les objets membres complets

            return ResponseEntity.ok(response);
        }
        return ResponseEntity.notFound().build();
    }


    @GetMapping("/{id}/taches")
    public ResponseEntity<List<Tache>> getTachesByProjet(@PathVariable Long id) {
        return ResponseEntity.ok(tacheRepository.findByProjetId(id));
    }

    @RequestMapping("get-all-by-id-ChefEquipe/{id}")
    public List<Projet> listProjetByChefEquipe(@PathVariable Long id){
        return projetService.getProjetByChefEquipe(id);
    }
    // ProjetController.java
    @GetMapping("/projet/{id}/chef-id")
    public ResponseEntity<Long> getChefIdByProjet(@PathVariable Long id) {
        Optional<Projet> projet = projetRepository.findById(id);
        return projet.map(p -> ResponseEntity.ok(p.getEquipe().getChefEquipe().getId()))
                .orElse(ResponseEntity.notFound().build());
    }

    // Dans ProjetRestController.java
    @PutMapping("/{id}/update-statut")
    public ResponseEntity<Projet> updateProjetStatut(@PathVariable Long id) {
        projetService.updateProjetStatut(id);
        return ResponseEntity.ok(projetRepository.findById(id).orElseThrow());
    }

    // Dans ProjetRestController.java
    @PutMapping("/{id}/update-statut-manuel")
    public ResponseEntity<Projet> updateProjetStatutManuel(
            @PathVariable Long id,
            @RequestBody Map<String, String> request) {

        String nouveauStatut = request.get("statut");
        Projet updatedProjet = projetService.updateProjetStatut(id, nouveauStatut);


        return ResponseEntity.ok(updatedProjet);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> supprimerProjet(@PathVariable Long id) {
        try {
            projetService.supprimerProjet(id);
            return ResponseEntity.ok().build();
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }



    @PostMapping("/{projetId}/taches")
    public ResponseEntity<?> ajouterTacheAuProjet(
            @PathVariable Long projetId,
            @RequestBody Tache nouvelleTache) {
        try {
            Tache tache = projetService.ajouterTacheAuProjet(projetId, nouvelleTache);
            return ResponseEntity.status(HttpStatus.CREATED).body(tache);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @PutMapping("/{projetId}/taches/{tacheId}")
    public ResponseEntity<?> modifierTacheProjet(
            @PathVariable Long projetId,
            @PathVariable Long tacheId,
            @RequestBody Tache tacheModifiee) {
        try {
            Tache tache = projetService.modifierTacheProjet(projetId, tacheId, tacheModifiee);
            return ResponseEntity.ok(tache);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }
    @RequestMapping(value = "/{id}" , method = RequestMethod.GET)
    public Optional<Projet> getProjetById(@PathVariable("id") Long id){

        Optional<Projet> projet = projetService.afficherProjetById(id);
        return projet;
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> modifierProjet(@PathVariable Long id, @RequestBody SaveProjet model) {
        try {
            // Charger le projet existant pour récupérer les infos non modifiables
            Projet existing = projetRepository.findById(id).orElseThrow();
            model.setIdClient(existing.getClient().getId());
            if (existing.getChefEquipe() != null) {
                model.setIdChefEquipe(existing.getChefEquipe().getId());
            }

            Projet updatedProjet = projetService.modifierProjet(id, model);
            return ResponseEntity.ok(updatedProjet);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }



    @PostMapping("/equipes")
    public ResponseEntity<Equipe> creerEquipe(
            @RequestParam String nomEquipe,
            @RequestParam String description,
            @RequestParam String domaineSpecialisation,
            @RequestParam(required = false) Long chefEquipeId,
            @RequestParam List<Long> developpeursIds) {

        Equipe equipe = projetService.creerEquipe(
                nomEquipe,
                description,
                domaineSpecialisation,
                chefEquipeId,
                developpeursIds
        );
        return ResponseEntity.ok(equipe);
    }

    @PostMapping("/equipes/{equipeId}/membres")
    public ResponseEntity<Equipe> ajouterMembre(
            @PathVariable Long equipeId,
            @RequestParam Long developpeurId) {

        Equipe equipe = projetService.ajouterMembreAEquipe(equipeId, developpeurId);
        return ResponseEntity.ok(equipe);
    }

    @DeleteMapping("/equipes/{equipeId}/membres/{developpeurId}")
    public ResponseEntity<Equipe> retirerMembre(
            @PathVariable Long equipeId,
            @PathVariable Long developpeurId) {
        Equipe equipe = projetService.retirerMembreDeEquipe(equipeId, developpeurId);
        return ResponseEntity.ok(equipe);
    }
}