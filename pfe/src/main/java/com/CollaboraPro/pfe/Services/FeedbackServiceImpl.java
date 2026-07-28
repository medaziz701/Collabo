package com.CollaboraPro.pfe.Services;

import com.CollaboraPro.pfe.Entity.Feedback;
import com.CollaboraPro.pfe.Repository.FeedbackRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FeedbackServiceImpl implements FeedbackService {


    @Autowired
    private FeedbackRepository feedbackRepository;

    @Override
    public Feedback AjouterFeedback(Feedback feedback) {
        return feedbackRepository.save(feedback);
    }

    @Override
    public List<Feedback> AfficherFeedback() {
        return feedbackRepository.findAll();
    }

    @Override
    public List<Feedback> AfficherFeedbackByProjetId(Long projetId) {
        return feedbackRepository.findByProjetId(projetId);
    }

    @Override
    public List<Feedback> AfficherFeedbackByClientId(Long clientId) {
        return feedbackRepository.findByClientId(clientId);
    }

    @Override
    public Feedback AfficherFeedbackById(Long feedbackId) {
        return feedbackRepository.findById(feedbackId).orElse(null);
    }

    @Override
    public void supprimerFeedback(Long feedbackId) {
        feedbackRepository.deleteById(feedbackId);
    }
}
