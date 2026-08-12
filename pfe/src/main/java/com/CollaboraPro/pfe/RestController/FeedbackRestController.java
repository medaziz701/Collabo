package com.CollaboraPro.pfe.RestController;

import com.CollaboraPro.pfe.Entity.Feedback;
import com.CollaboraPro.pfe.Services.FeedbackService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin("*")
@RequestMapping(value = "/feedback")
public class FeedbackRestController {

    @Autowired
    private FeedbackService feedbackService;

    @PostMapping
    public Feedback ajouterFeedback(@RequestBody Feedback feedback) {
        return feedbackService.AjouterFeedback(feedback);
    }

    @GetMapping
    public List<Feedback> afficherTous() {
        return feedbackService.AfficherFeedback();
    }

    @GetMapping("/projet/{projetId}")
    public List<Feedback> afficherParProjet(@PathVariable Long projetId) {
        return feedbackService.AfficherFeedbackByProjetId(projetId);
    }

    @GetMapping("/client/{clientId}")
    public List<Feedback> afficherParClient(@PathVariable Long clientId) {
        return feedbackService.AfficherFeedbackByClientId(clientId);
    }

    @GetMapping("/{feedbackId}")
    public Feedback afficherParId(@PathVariable Long feedbackId) {
        return feedbackService.AfficherFeedbackById(feedbackId);
    }

    @DeleteMapping("/{feedbackId}")
    public void supprimer(@PathVariable Long feedbackId) {
        feedbackService.supprimerFeedback(feedbackId);
    }
}