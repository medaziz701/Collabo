package com.CollaboraPro.pfe.Repository;

import com.CollaboraPro.pfe.Entity.Equipe;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface EquipeRepository extends JpaRepository<Equipe , Long> {

    List<Equipe> findByChefEquipe_Id(Long chefId);

    List<Equipe> findByMembresId(Long developpeurId);


}
