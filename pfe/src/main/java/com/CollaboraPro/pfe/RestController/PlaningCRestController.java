package com.CollaboraPro.pfe.RestController;

import com.CollaboraPro.pfe.Entity.PlanningC;
import com.CollaboraPro.pfe.Entity.SavePlaningC;
import com.CollaboraPro.pfe.Repository.PlanningCRepository;
import com.CollaboraPro.pfe.Services.PlanningCService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;


@RestController
@CrossOrigin("*")
@RequestMapping("/planningC")
public class PlaningCRestController {
    @Autowired
    PlanningCService planningCService;

    @Autowired
    PlanningCRepository planningCRepository;

    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<?> ajouterRendezvousChefEquipe(@RequestBody SavePlaningC model) {
        PlanningC planningC = SavePlaningC.toEntity(model);

        if (planningCRepository.existsByDateAndDebutAndFinAndChefEquipe_Id(
                model.getDate(), model.getDebut(), model.getFin(), model.getIdChefEquipe())) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Ce rendez-vous existe déjà pour ce Chef d'Equipe !");
        } else {
            PlanningC savedPlanningC= planningCService.ajouterPlanningC(model);
            return ResponseEntity.status(HttpStatus.CREATED).body(savedPlanningC);
        }
    }

    @RequestMapping(method = RequestMethod.GET)
    public List<PlanningC> afficherPlanningC() {
        return  planningCService.afficherPlanningC();
    }

    @RequestMapping("get-all-by-id-chefEquipe/{idChefEquipe}")
    public List<PlanningC> listPlanningCByChefEquipe(@PathVariable Long idChefEquipe) {
        return planningCService.getPlanningCByChefEquipeId(idChefEquipe);
    }
    @GetMapping("/get-by-developpeur-id/{developpeurId}")
    public ResponseEntity<List<PlanningC>> listPlanningCByDeveloppeurId(@PathVariable Long developpeurId) {
        List<PlanningC> plannings = planningCService.getPlanningCByDeveloppeurId(developpeurId);
        return ResponseEntity.ok(plannings);
    }
}