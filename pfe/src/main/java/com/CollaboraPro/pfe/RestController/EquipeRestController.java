package com.CollaboraPro.pfe.RestController;

import com.CollaboraPro.pfe.Entity.Admin;
import com.CollaboraPro.pfe.Entity.Developpeur;
import com.CollaboraPro.pfe.Entity.Equipe;
import com.CollaboraPro.pfe.Repository.DeveloppeurRepository;
import com.CollaboraPro.pfe.Repository.EquipeRepository;
import com.CollaboraPro.pfe.Services.EquipeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@CrossOrigin("*")
@RequestMapping("/equipe")
public class EquipeRestController {

    @Autowired
    EquipeService equipeService;

    @Autowired
    EquipeRepository equipeRepository;

    @Autowired
    DeveloppeurRepository developpeurRepository;

    @PostMapping
    public ResponseEntity<?> ajouterEquipe(@RequestBody Equipe equipe) {
        equipe.setDateCreation(LocalDate.now());
        equipe.setDateDerniereModification(LocalDate.now());
        Equipe savedEquipe = equipeService.ajouterEquipe(equipe);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedEquipe);
    }



    @PutMapping("/{id}")
    public ResponseEntity<?> modifierEquipe(@PathVariable Long id, @RequestBody Equipe equipe) {
        equipe.setId(id);
        equipe.setDateDerniereModification(LocalDate.now());
        Equipe updatedEquipe = equipeService.modifierEquipe(equipe);
        return ResponseEntity.ok(updatedEquipe);
    }

    @GetMapping("/{id}/details")
    public ResponseEntity<?> getEquipeDetails(@PathVariable Long id) {
        Optional<Equipe> equipe = equipeService.afficherEquipeById(id);
        if (equipe.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        Map<String, Object> response = new HashMap<>();
        response.put("equipe", equipe.get());
        response.put("nombreMembres", equipe.get().getNombreMembres());
        response.put("membresInfo", equipe.get().getMembresInfo());
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> supprimerEquipe(@PathVariable Long id) {
        equipeService.supprimerEquipe(id);
        return ResponseEntity.ok("Équipe supprimée avec succès");
    }

    @GetMapping
    public List<Equipe> afficherEquipes() {
        return equipeService.afficherEquipe();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> afficherEquipeById(@PathVariable Long id) {
        Optional<Equipe> equipe = equipeService.afficherEquipeById(id);
        if (equipe.isPresent()) {
            return ResponseEntity.ok(equipe.get());
        } else {
            Map<String, String> response = new HashMap<>();
            response.put("message", "Équipe non trouvée");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
    }
    @GetMapping("/chef/{chefId}")
    public List<Equipe> trouverEquipesParChef(@PathVariable Long chefId) {
        return equipeService.trouverEquipesParChef(chefId);
    }

    @PostMapping("/{equipeId}/membres/{developpeurId}")
    public ResponseEntity<?> ajouterMembre(@PathVariable Long equipeId, @PathVariable Long developpeurId) {
        Equipe equipe = equipeRepository.findById(equipeId).orElse(null);
        Developpeur dev = developpeurRepository.findById(developpeurId).orElse(null);

        if (equipe == null || dev == null) {
            return ResponseEntity.notFound().build();
        }

        dev.setDisponibilite(false);
        developpeurRepository.save(dev);
        equipe.getMembres().add(dev);
        equipeRepository.save(equipe);

        return ResponseEntity.ok("Membre ajouté avec succès");
    }

    @DeleteMapping("/{equipeId}/membres/{developpeurId}")
    public ResponseEntity<?> supprimerMembre(@PathVariable Long equipeId, @PathVariable Long developpeurId) {
        equipeService.supprimerMembreDeEquipe(equipeId, developpeurId);
        return ResponseEntity.ok("Membre retiré de l'équipe avec succès");
    }
}