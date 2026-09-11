package com.CollaboraPro.pfe.Services;

import com.CollaboraPro.pfe.Entity.Feedback;

import java.util.List;

public interface FeedbackService {
    Feedback AjouterFeedback(Feedback feedback);
    List<Feedback> AfficherFeedback();
    List<Feedback> AfficherFeedbackByProjetId(Long projetId);
    List<Feedback> AfficherFeedbackByClientId(Long clientId);
    Feedback AfficherFeedbackById(Long feedbackId) ;
    void supprimerFeedback(Long feedbackId)  ;
}
