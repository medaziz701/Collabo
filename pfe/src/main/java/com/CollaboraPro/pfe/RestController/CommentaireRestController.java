package com.CollaboraPro.pfe.RestController;

import com.CollaboraPro.pfe.Entity.Commentaire;
import com.CollaboraPro.pfe.Services.CommentaireService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@CrossOrigin("*")
@RequestMapping("/commentaires")
public class CommentaireRestController {
    @Autowired
    private CommentaireService commentaireService;

    @PostMapping
    public ResponseEntity<?> creerCommentaire(@RequestBody Map<String, Object> request) {
        try {
            Long codePartId = Long.valueOf(request.get("codePartId").toString());
            String contenu = request.get("contenu").toString();
            String auteur = request.get("auteur").toString();

            Commentaire commentaire = commentaireService.creerCommentaire(
                    codePartId,
                    contenu,
                    auteur
            );

            return ResponseEntity.ok(commentaire);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Erreur: " + e.getMessage());
        }
    }

    @GetMapping("/code/{codePartId}")
    public List<Commentaire> getCommentairesParCode(@PathVariable Long codePartId) {
        return commentaireService.getCommentairesParCode(codePartId);
    }

    @GetMapping("/chef/{chefEquipeId}")
    public List<Commentaire> getCommentairesPourChef(@PathVariable Long chefEquipeId) {
        return commentaireService.getCommentairesPourChef(chefEquipeId);
    }
    // CommentaireRestController.java
    @GetMapping("/tache/{tacheId}")
    public ResponseEntity<List<Commentaire>> getCommentairesParTache(@PathVariable Long tacheId) {
        return ResponseEntity.ok(commentaireService.getCommentairesParTache(tacheId));
    }
}
