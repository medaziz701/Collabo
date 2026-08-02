package com.CollaboraPro.pfe.RestController;

import com.CollaboraPro.pfe.Entity.Admin;
import com.CollaboraPro.pfe.Entity.ChefEquipe;
import com.CollaboraPro.pfe.Entity.Developpeur;
import com.CollaboraPro.pfe.Repository.AdminRepository;
import com.CollaboraPro.pfe.Repository.ChefEquipeRepository;
import com.CollaboraPro.pfe.Services.ChefEquipeService;
import com.CollaboraPro.pfe.Services.EmailDService;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@CrossOrigin("*")
@RequestMapping(value = "/chefEquipe")
public class ChefEquipeRestController {
    private BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();

    @Autowired
    ChefEquipeService chefEquipeService;

    @Autowired
    ChefEquipeRepository chefEquipeRepository;

    @Autowired
    EmailDService emailDService;



    @Autowired
    AdminRepository adminRepository;

    @RequestMapping(method = RequestMethod.POST)
    ResponseEntity<?> AjouterChefEquipe(@RequestBody ChefEquipe chefEquipe) {
        HashMap<String, Object> response = new HashMap<>();


        if(chefEquipeRepository.existsByEmail(chefEquipe.getEmail())) {
            response.put("message", "email exist deja !");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        } else {
            Admin admin = new Admin();
            admin.setId(1L); // ID fixe de admin unique
            chefEquipe.setCreatedByAdmin(admin);
            chefEquipe.setEtat(true);
            // Sauvegarde du mot de passe en clair temporairement pour l'email
            String plainPassword = chefEquipe.getMp();

            // Hash et sauvegarde du chef d'équipe
            chefEquipe.setMp(this.bCryptPasswordEncoder.encode(plainPassword));
            ChefEquipe savedUser = chefEquipeRepository.save(chefEquipe);

            // Envoi de l'email
            String subject = "Vos identifiants pour votre compte CollaboraPro (Chef d'Équipe)";
            String body = "Bonjour " + savedUser.getPrenom() + ",\n\n"
                    + "Votre compte chef d'équipe a été créé avec succès.\n"
                    + "Voici vos identifiants de connexion :\n"
                    + "Email: " + savedUser.getEmail() + "\n"
                    + "Mot de passe: " + plainPassword + "\n\n"
                    + "Vous pouvez vous connecter à l'adresse: http://localhost:4200//loginChefEquipe\n\n"
                    + "Cordialement,\nL'équipe CollaboraPro";

            emailDService.SendSimpleMessage(savedUser.getEmail(), subject, body);


            return ResponseEntity.status(HttpStatus.CREATED).body(savedUser);

        }
    }




    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    public ChefEquipe modifierchefEquipe(@PathVariable("id") Long id, @RequestBody ChefEquipe chefEquipe) {
        return chefEquipeRepository.findById(id).map(existingChef -> {
            // Mise à jour des champs de base
            existingChef.setNom(chefEquipe.getNom());
            existingChef.setPrenom(chefEquipe.getPrenom());
            existingChef.setTlf(chefEquipe.getTlf());
            existingChef.setEmail(chefEquipe.getEmail());
            existingChef.setAdresse(chefEquipe.getAdresse());
            existingChef.setGenre(chefEquipe.getGenre());
            existingChef.setCin(chefEquipe.getCin());
            existingChef.setDateNaissance(chefEquipe.getDateNaissance());
            existingChef.setDatePriseFonction(chefEquipe.getDatePriseFonction());

            // Ne re-hasher le mot de passe QUE s'il est explicitement fourni et non vide
            if (chefEquipe.getMp() != null && !chefEquipe.getMp().isEmpty()
                    && !bCryptPasswordEncoder.matches(chefEquipe.getMp(), existingChef.getMp())) {
                existingChef.setMp(bCryptPasswordEncoder.encode(chefEquipe.getMp()));
            }

            // Gestion de l'état
            if (chefEquipe.isEtat() != existingChef.isEtat()) {
                String etat = chefEquipe.isEtat() ? "Activé" : "Bloqué";
                emailDService.SendSimpleMessage(existingChef.getEmail(),
                        "État de votre compte",
                        "Votre compte a été " + etat);
                existingChef.setEtat(chefEquipe.isEtat());
            }

            return chefEquipeRepository.save(existingChef);
        }).orElse(null);
    }



    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE )
    public void suppChefEquipe(@PathVariable("id") Long id){
        chefEquipeService.supprimerChefEquipe(id);

    }



    @RequestMapping(method = RequestMethod.GET )
    public List<ChefEquipe> afficherChefEquipe(){
        return chefEquipeService.afficherChefEquipe();

    }


    @PostMapping("/login")
    public ResponseEntity<Map<String, Object>> loginChefEquipe(@RequestBody ChefEquipe chefEquipe) {
        System.out.println("in login-chefEquipe"+chefEquipe);
        HashMap<String, Object> response = new HashMap<>();

        ChefEquipe userFromDB = chefEquipeRepository.findChefEquipeByEmail(chefEquipe.getEmail());
        System.out.println("userFromDB+chefEquipe"+userFromDB);
        if (userFromDB == null) {
            response.put("message", "ChefEquipe not found !");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        } else {
            // Vérifier d'abord l'état du compte
            if (!userFromDB.isEtat()) {
                response.put("message", "Votre compte n'est pas encore activé par l'administrateur");
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
            }

            boolean compare = this.bCryptPasswordEncoder.matches(chefEquipe.getMp(), userFromDB.getMp());
            System.out.println("compare"+compare);
            if (!compare) {
                response.put("message", "Email ou mot de passe incorrect !");
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
            } else {
                String token = Jwts.builder()
                        .claim("data", userFromDB)
                        .signWith(SignatureAlgorithm.HS256, "SECRET")
                        .compact();
                response.put("token", token);
                return ResponseEntity.status(HttpStatus.OK).body(response);
            }
        }
    }

    @RequestMapping(value = "/{id}" , method = RequestMethod.GET)
    public Optional<ChefEquipe> getChefEquipeById(@PathVariable("id") Long id){

        Optional<ChefEquipe> chefEquipe = chefEquipeService.afficherChefEquipeById(id);
        return chefEquipe;
    }






}
