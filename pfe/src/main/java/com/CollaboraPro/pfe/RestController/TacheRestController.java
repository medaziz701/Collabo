package com.CollaboraPro.pfe.RestController;

import com.CollaboraPro.pfe.Entity.Tache;
import com.CollaboraPro.pfe.Services.TacheService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@CrossOrigin("*")
@RequestMapping(value = "/tache")
public class TacheRestController {

    @Autowired
    private TacheService tacheService;

    @PostMapping
    public ResponseEntity<?> ajouterTache(@RequestBody Tache tache) {
        Tache savedTache = tacheService.ajouterTache(tache);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedTache);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> modifierTache(@PathVariable Long id, @RequestBody Tache tache) {
        if (!tacheService.afficherTacheById(id).isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Tache not found");
        }
        tache.setId(id);
        Tache updatedTache = tacheService.modifierTache(tache);
        return ResponseEntity.ok(updatedTache);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> supprimerTache(@PathVariable Long id) {
        if (!tacheService.afficherTacheById(id).isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Tache not found");
        }
        tacheService.supprimerTache(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping
    public List<Tache> afficherTaches() {
        return tacheService.afficherTaches();
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> afficherTacheById(@PathVariable Long id) {
        Optional<Tache> tache = tacheService.afficherTacheById(id);
        if (tache.isPresent()) {
            return ResponseEntity.ok(tache.get());
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Tache not found");
        }
    }

    // Dans TacheController.java
    @PutMapping("/{id}/terminer")
    public ResponseEntity<Tache> marquerTacheTerminee(@PathVariable Long id) {
        return ResponseEntity.ok(tacheService.marquerTacheTerminee(id));
    }

    @GetMapping("/projet/{projetId}/toutesTerminees")
    public ResponseEntity<Boolean> toutesTachesTerminees(@PathVariable Long projetId) {
        return ResponseEntity.ok(tacheService.toutesTachesTerminees(projetId));
    }


}