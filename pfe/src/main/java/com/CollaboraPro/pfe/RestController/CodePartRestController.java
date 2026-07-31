package com.CollaboraPro.pfe.RestController;

import com.CollaboraPro.pfe.Entity.CodePart;
import com.CollaboraPro.pfe.Entity.Projet;
import com.CollaboraPro.pfe.Entity.Tache;
import com.CollaboraPro.pfe.Repository.CodePartRepository;
import com.CollaboraPro.pfe.Repository.ProjetRepository;
import com.CollaboraPro.pfe.Repository.TacheRepository;
import com.CollaboraPro.pfe.Services.CodePartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@CrossOrigin("*")
@RequestMapping("/code")
public class CodePartRestController {
    @Autowired
    CodePartService codePartService;

    @Autowired
    CodePartRepository codePartRepository;

    @Autowired
    TacheRepository tacheRepository;

    @Autowired
    ProjetRepository projetRepository;

    @PostMapping
    public ResponseEntity<?> addCodePart(@RequestBody Map<String, Object> request) {
        try {
            // Vérification des champs obligatoires
            if (!request.containsKey("tache") || !request.containsKey("filename")
                    || !request.containsKey("content") || !request.containsKey("author")) {
                return ResponseEntity.badRequest().body("Les champs obligatoires sont manquants");
            }

            // Extraction de l'ID de tâche
            Map<String, Object> tacheMap = (Map<String, Object>) request.get("tache");
            if (tacheMap == null || tacheMap.get("id") == null) {
                return ResponseEntity.badRequest().body("L'ID de tâche est manquant");
            }
            Long tacheId = Long.valueOf(tacheMap.get("id").toString());
            Tache tache = tacheRepository.findById(tacheId)
                    .orElseThrow(() -> new RuntimeException("Tâche non trouvée"));

            // Extraction de l'ID de projet
            Map<String, Object> projetMap = (Map<String, Object>) request.get("projet");
            if (projetMap == null || projetMap.get("id") == null) {
                return ResponseEntity.badRequest().body("L'ID de projet est manquant");
            }
            Long projetId = Long.valueOf(projetMap.get("id").toString());
            Projet projet = projetRepository.findById(projetId)
                    .orElseThrow(() -> new RuntimeException("Projet non trouvé"));

            // Création du CodePart
            CodePart codePart = new CodePart();
            codePart.setFilename(request.get("filename").toString());
            codePart.setContent(request.get("content").toString());
            codePart.setAuthor(request.get("author").toString());
            codePart.setTache(tache);
            codePart.setProjet(projet); // Ajout du projet

            return ResponseEntity.ok(codePartRepository.save(codePart));

        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Erreur lors de la création: " + e.getMessage());
        }
    }
    @GetMapping("/projet/{projetId}")
    public ResponseEntity<List<CodePart>> getCodePartsByProjet(@PathVariable Long projetId) {
        try {
            List<CodePart> codeParts = codePartRepository.findByProjetId(projetId);
            return ResponseEntity.ok(codeParts);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping("/tache/{tacheId}")
    public ResponseEntity<List<CodePart>> getCodePartsByTache(@PathVariable Long tacheId) {
        try {
            List<CodePart> codeParts = codePartService.getCodePartsByTache(tacheId);
            return ResponseEntity.ok(codeParts);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }



    @GetMapping
    public ResponseEntity<List<CodePart>> getAllCodeParts() {
        try {
            List<CodePart> codeParts = codePartRepository.findAll();
            return ResponseEntity.ok(codeParts);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }




    @PutMapping("/{id}")
    public ResponseEntity<CodePart> updateCodePart(@PathVariable Long id, @RequestBody CodePart updatedPart) {
        try {
            CodePart part = codePartRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("CodePart not found"));

            part.setContent(updatedPart.getContent());
            CodePart savedPart = codePartRepository.save(part);
            return ResponseEntity.ok(savedPart);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    // DELETE - supprimer
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCodePart(@PathVariable Long id) {
        try {
            codePartRepository.deleteById(id);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }






}