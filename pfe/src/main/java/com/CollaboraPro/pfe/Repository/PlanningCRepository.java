package com.CollaboraPro.pfe.Repository;



import com.CollaboraPro.pfe.Entity.ChefEquipe;
import com.CollaboraPro.pfe.Entity.PlanningC;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Date;
import java.util.List;

public interface PlanningCRepository extends JpaRepository <PlanningC,Long>{


     boolean existsByDateAndDebutAndFinAndChefEquipe_Id(Date date, String debut, String fin, Long idChefEquipe) ;

    List<PlanningC> findByChefEquipeIn(List<ChefEquipe> chefs);
    List<PlanningC> findByChefEquipe_Id(Long idChefEquipe);
}