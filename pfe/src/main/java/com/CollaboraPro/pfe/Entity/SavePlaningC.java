package com.CollaboraPro.pfe.Entity;

import lombok.Data;

import java.util.Date;


@Data
public class SavePlaningC {

    private Long id;
    private String nom;
    private Date date;
    private String debut;
    private String fin;
    private Long idChefEquipe;



    public static PlanningC toEntity(SavePlaningC model)
    {
        if(model == null)
        {
            return null ;
        }
        PlanningC planningC=new PlanningC();
        planningC.setId(model.getId());
        planningC.setNom(model.getNom());
        planningC.setDate(model.getDate());
        planningC.setDebut(model.getDebut());
        planningC.setFin(model.getFin());

        return planningC;
}
}
