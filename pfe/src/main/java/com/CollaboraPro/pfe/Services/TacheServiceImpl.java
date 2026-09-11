package com.CollaboraPro.pfe.Services;

import com.CollaboraPro.pfe.Entity.Tache;
import com.CollaboraPro.pfe.Repository.TacheRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TacheServiceImpl implements TacheService {

    @Autowired
    private TacheRepository tacheRepository;

    @Autowired
    private ProjetService projetService;

    public Tache marquerTacheTerminee(Long tacheId) {
        Tache tache = tacheRepository.findById(tacheId)
                .orElseThrow(() -> new RuntimeException("Tâche non trouvée"));

        tache.setStatut(Tache.StatutTache.TERMINEE);
        Tache savedTache = tacheRepository.save(tache);

        // Vérifier si toutes les tâches sont terminées et mettre à jour le projet
        projetService.updateProjetStatut(tache.getProjet().getId());

        return savedTache;
    }

    @Override
    public Tache ajouterTache(Tache tache) {
        return tacheRepository.save(tache);
    }

    @Override
    public Tache modifierTache(Tache tache) {
        return tacheRepository.save(tache);
    }

    @Override
    public void supprimerTache(Long id) {
        tacheRepository.deleteById(id);
    }

    @Override
    public List<Tache> afficherTaches() {
        return tacheRepository.findAll();
    }

    @Override
    public Optional<Tache> afficherTacheById(Long id) {
        return tacheRepository.findById(id);
    }


    public Boolean toutesTachesTerminees(Long projetId) {
        List<Tache> taches = tacheRepository.findByProjetId(projetId);
        return !taches.isEmpty() && taches.stream()
                .allMatch(t -> t.getStatut() == Tache.StatutTache.TERMINEE);
    }
}