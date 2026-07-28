package com.CollaboraPro.pfe.Services;


import com.CollaboraPro.pfe.Entity.ChefEquipe;
import com.CollaboraPro.pfe.Entity.Equipe;
import com.CollaboraPro.pfe.Entity.PlanningC;
import com.CollaboraPro.pfe.Entity.SavePlaningC;
import com.CollaboraPro.pfe.Repository.ChefEquipeRepository;
import com.CollaboraPro.pfe.Repository.EquipeRepository;
import com.CollaboraPro.pfe.Repository.PlanningCRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class PlanningCServiceImpl implements PlanningCService{
    @Autowired
    PlanningCRepository planningCRepository;

    @Autowired
    ChefEquipeRepository chefEquipeRepository;


    public PlanningC ajouterPlanningC(SavePlaningC model) {
        PlanningC PlaningC = SavePlaningC.toEntity(model);
        System.out.println("idChef: " + model.getIdChefEquipe());
        ChefEquipe chefEquipe = chefEquipeRepository.findById(model.getIdChefEquipe()).get();
        PlaningC.setChefEquipe(chefEquipe);

        return planningCRepository.save(PlaningC);
    }

    @Override

        public List<PlanningC> afficherPlanningC() {
            return planningCRepository.findAll();
        }


    @Override
    public Optional<PlanningC> afficherPlanningCById(Long id) {
            return planningCRepository.findById(id);


    }

    @Override
    public List<PlanningC> getPlanningCByChefEquipeId(Long idChefEquipe) {
        return planningCRepository.findByChefEquipe_Id(idChefEquipe);
    }

    @Autowired
    private EquipeRepository equipeRepository;

    public List<PlanningC> getPlanningCByDeveloppeurId(Long developpeurId) {
        // 1. Récupérer toutes les équipes du développeur
        List<Equipe> equipes = equipeRepository.findByMembresId(developpeurId);

        // 2. Extraire les chefs d'équipe
        List<ChefEquipe> chefs = equipes.stream()
                .map(Equipe::getChefEquipe)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());

        // 3. Récupérer les plannings de ces chefs
        return planningCRepository.findByChefEquipeIn(chefs);
    }




}