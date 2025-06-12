package com.CollaboraPro.pfe.Repository;

import com.CollaboraPro.pfe.Entity.ChefEquipe;
import com.CollaboraPro.pfe.Entity.Developpeur;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChefEquipeRepository extends JpaRepository<ChefEquipe , Long> {

    ChefEquipe findChefEquipeByEmail(String email);

    boolean existsByEmail(String email);

}
