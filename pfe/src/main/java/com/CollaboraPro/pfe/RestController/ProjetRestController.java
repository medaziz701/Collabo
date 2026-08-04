package com.CollaboraPro.pfe.RestController;

import com.CollaboraPro.pfe.Entity.*;
import com.CollaboraPro.pfe.Repository.CodePartRepository;
import com.CollaboraPro.pfe.Repository.DeveloppeurRepository;
import com.CollaboraPro.pfe.Repository.EquipeRepository;
import com.CollaboraPro.pfe.Repository.ProjetRepository;
import com.CollaboraPro.pfe.Repository.TacheRepository;
import com.CollaboraPro.pfe.Services.NotificationService;
import com.CollaboraPro.pfe.Services.ProjetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.ByteArrayOutputStream;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

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
    CodePartRepository codePartRepository;

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
    public ResponseEntity<Optional<Projet>> getProjetById(@PathVariable("id") Long id){
        Optional<Projet> projet = projetService.afficherProjetById(id);
        if (projet.isPresent()) {
            return ResponseEntity.ok(projet);
        } else {
            return ResponseEntity.notFound().build();
        }
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

    @PostMapping("/{id}/soumettre-livrable")
    public ResponseEntity<?> soumettreLivrable(@PathVariable Long id, @RequestBody Map<String, Object> request) {
        try {
            Long chefEquipeId = Long.valueOf(request.get("chefEquipeId").toString());

            Projet projet = projetRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("Projet non trouvé"));

            // Vérifier que le chef d'équipe est bien assigné à ce projet
            if (projet.getChefEquipe() == null || !projet.getChefEquipe().getId().equals(chefEquipeId)) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN)
                        .body("Vous n'êtes pas autorisé à soumettre le livrable pour ce projet");
            }

            // Vérifier que le livrable n'a pas déjà été soumis
            if (projet.isLivraisonValidee()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body("Le livrable a déjà été soumis");
            }

            // Marquer comme validé
            projet.setLivraisonValidee(true);
            projet.setDateLivraison(LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));

            projetRepository.save(projet);

            return ResponseEntity.ok(projet);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erreur lors de la soumission du livrable: " + e.getMessage());
        }
    }

    @GetMapping("/{id}/download")
    public ResponseEntity<byte[]> downloadProjet(@PathVariable Long id, @RequestParam Long clientId) {
        try {
            Projet projet = projetRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("Projet non trouvé"));

            // Vérifier que le livrable a été validé
            if (!projet.isLivraisonValidee()) {
                throw new RuntimeException("Le chef d'équipe n'a pas encore validé la livraison");
            }

            // Vérifier que l'appelant est bien le client propriétaire du projet
            if (projet.getClient() == null || !projet.getClient().getId().equals(clientId)) {
                throw new RuntimeException("Vous n'êtes pas autorisé à télécharger ce projet");
            }

            // Récupérer tous les CodeParts du projet
            List<CodePart> codeParts = codePartRepository.findByProjetIdWithRelations(id);

            if (codeParts.isEmpty()) {
                throw new RuntimeException("Aucun code disponible pour ce projet");
            }

            // Générer le ZIP
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            ZipOutputStream zipOutputStream = new ZipOutputStream(byteArrayOutputStream);

            // Grouper par auteur pour éviter les collisions de noms
            Map<String, List<CodePart>> codePartsByAuthor = codeParts.stream()
                    .collect(Collectors.groupingBy(cp -> cp.getAuthor() != null ? cp.getAuthor() : "unknown"));

            for (Map.Entry<String, List<CodePart>> entry : codePartsByAuthor.entrySet()) {
                String author = entry.getKey();
                List<CodePart> authorCodeParts = entry.getValue();

                for (CodePart codePart : authorCodeParts) {
                    String filename = codePart.getFilename();
                    String entryName = author + "/" + filename;

                    ZipEntry zipEntry = new ZipEntry(entryName);
                    zipOutputStream.putNextEntry(zipEntry);

                    if (codePart.getContent() != null) {
                        zipOutputStream.write(codePart.getContent().getBytes());
                    }

                    zipOutputStream.closeEntry();
                }
            }

            zipOutputStream.close();

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
            headers.setContentDispositionFormData("attachment", projet.getNom() + ".zip");

            return ResponseEntity.ok()
                    .headers(headers)
                    .body(byteArrayOutputStream.toByteArray());

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @DeleteMapping("/equipes/{equipeId}/membres/{developpeurId}")
    public ResponseEntity<Equipe> retirerMembre(
            @PathVariable Long equipeId,
            @PathVariable Long developpeurId) {
        Equipe equipe = projetService.retirerMembreDeEquipe(equipeId, developpeurId);
        return ResponseEntity.ok(equipe);
    }
}