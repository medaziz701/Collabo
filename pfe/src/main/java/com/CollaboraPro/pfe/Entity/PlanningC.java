package com.CollaboraPro.pfe.Entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;

import java.util.Date;

@Entity
@Data
public class PlanningC {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String nom;
    private Date date;
    private String debut;
    private String fin;



    @ManyToOne
    @JoinColumn(name = "chef_equipe_id")
    private ChefEquipe chefEquipe;
}