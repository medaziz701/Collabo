package com.CollaboraPro.pfe.Services;




import com.CollaboraPro.pfe.Entity.PlanningC;
import com.CollaboraPro.pfe.Entity.SavePlaningC;

import java.util.List;
import java.util.Optional;

public interface PlanningCService {



     PlanningC ajouterPlanningC(SavePlaningC model);

     List<PlanningC> afficherPlanningC();
    Optional<PlanningC> afficherPlanningCById(Long id);
    List<PlanningC> getPlanningCByChefEquipeId(Long idChefEquipe);
    public List<PlanningC> getPlanningCByDeveloppeurId(Long developpeurId);
}