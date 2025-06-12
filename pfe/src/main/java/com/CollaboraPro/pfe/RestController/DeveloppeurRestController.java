package com.CollaboraPro.pfe.RestController;

import com.CollaboraPro.pfe.Entity.*;
import com.CollaboraPro.pfe.Repository.DeveloppeurRepository;
import com.CollaboraPro.pfe.Repository.EquipeRepository;
import com.CollaboraPro.pfe.Services.ChefEquipeService;
import com.CollaboraPro.pfe.Services.DeveloppeurService;
import com.CollaboraPro.pfe.Services.EmailDService;
import com.CollaboraPro.pfe.Services.NotificationService;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@CrossOrigin("*")
@RequestMapping(value = "/developpeur")
public class DeveloppeurRestController {
    private BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();

    @Autowired
    EmailDService emailDService;

    @Autowired
    DeveloppeurService developpeurService;

    @Autowired
    DeveloppeurRepository developpeurRepository;

    @Autowired
    EquipeRepository equipeRepository;



    @Autowired
    NotificationService notificationService;



    @RequestMapping(method = RequestMethod.POST)
    ResponseEntity<?> AjouterDeveloppeur(@RequestBody Developpeur developpeur) {
        HashMap<String, Object> response = new HashMap<>();

        // Vérifiez d'abord l'email
        if (developpeurRepository.existsByEmail(developpeur.getEmail())) {
            response.put("message", "Email existe déjà !");
            return ResponseEntity.status(HttpStatus.CONFLICT).body(response);//retourne une statut erreur si c'est le cas
        } else {
            Admin admin = new Admin();
            admin.setId(1L);
            developpeur.setManagedByAdmin(admin);
            notificationService.creerNotification(
                    admin,
                    NotificationType.NOUVEAU_DEVELOPPEUR,
                    " Un nouveau développeur nommé " + developpeur.getNom() + " est inscrit"

            );
            // Encodez le mot de passe seulement après la vérification
            developpeur.setMp(this.bCryptPasswordEncoder.encode(developpeur.getMp()));
            return ResponseEntity.status(HttpStatus.CREATED).body( developpeurService.ajouterDeveloppeur(developpeur));//Sauvegarde le développeur via le service
//Retourne le développeur créé avec statut
        }
    }






    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    public Developpeur modifierdeveloppeur(@PathVariable("id") Long id, @RequestBody Developpeur developpeur) {
        return developpeurRepository.findById(id).map(existingDev -> {
            // Mise à jour des champs de base
            existingDev.setNom(developpeur.getNom());
            existingDev.setPrenom(developpeur.getPrenom());
            existingDev.setTlf(developpeur.getTlf());
            existingDev.setEmail(developpeur.getEmail());
            existingDev.setAdresse(developpeur.getAdresse());
            existingDev.setGenre(developpeur.getGenre());
            existingDev.setSpecialite(developpeur.getSpecialite());

            // Ne re-hasher le mot de passe QUE s'il est explicitement fourni et non vide
            if (developpeur.getMp() != null && !developpeur.getMp().isEmpty()
                    && !bCryptPasswordEncoder.matches(developpeur.getMp(), existingDev.getMp())) {
                existingDev.setMp(bCryptPasswordEncoder.encode(developpeur.getMp()));
            }

            // Gestion de l'état
            if (developpeur.isEtat() != existingDev.isEtat()) {
                String etat = developpeur.isEtat() ? "Accepté" : "Bloqué";
                emailDService.SendSimpleMessage(existingDev.getEmail(),
                        "État de votre compte",
                        "Votre compte a été " + etat);
                existingDev.setEtat(developpeur.isEtat());
            }

            return developpeurRepository.save(existingDev);
        }).orElse(null);
    }


    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE )
    public void suppDeveloppeur(@PathVariable("id") Long id){
        developpeurService.supprimerDeveloppeur(id);

    }



    @RequestMapping(method = RequestMethod.GET )
    public List<Developpeur> afficherDeveloppeur(){
        return developpeurService.afficherDeveloppeur();

    }
    @RequestMapping(value = "/{id}" , method = RequestMethod.GET)
    public Optional<Developpeur> getDeveloppeurById(@PathVariable("id") Long id){

        Optional<Developpeur> developpeur = developpeurService.afficherDeveloppeurById(id);
        return developpeur;
    }



    @PostMapping("/login")
    public ResponseEntity<Map<String, Object>> loginDeveloppeur(@RequestBody Developpeur developpeur) {// Retournera une réponse HTTP avec un corps au format Map
        System.out.println("in login-developpeur"+developpeur);//Affiche dans la console les données reçues (pour débogage)
        HashMap<String, Object> response = new HashMap<>();//Crée une Map pour construire la réponse JSON

        Developpeur userFromDB = developpeurRepository.findDeveloppeurByEmail(developpeur.getEmail());
        System.out.println("userFromDB+developpeur"+userFromDB);
        if (userFromDB == null) {
            response.put("message", "Developpeur not found !");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);//Retourne un statut HTTP 404 (NOT_FOUND)
        }
        else {
            // Vérifier d'abord l'état du compte
            if (!userFromDB.isEtat()) {
                response.put("message", "Votre compte n'est pas encore activé par l'administrateur");
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
            }
            boolean compare = this.bCryptPasswordEncoder.matches(developpeur.getMp(), userFromDB.getMp());//Compare le mot de passe fourni avec le hash stocké en base
            System.out.println("compare"+compare);//matches() prend en paramètres :le hash BCrypt stocké et mot de passe en clair
            if (!compare) {
                response.put("message", "developpeur not found !");
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);//Même réponse que si l'email n'existe pas (pour ne pas révéler d'info)
            }else
            {
                Map<String, Object> clientData = new HashMap<>();
                clientData.put("id", userFromDB.getId());
                clientData.put("email", userFromDB.getEmail());
                clientData.put("nom", userFromDB.getNom());
                clientData.put("prenom", userFromDB.getPrenom());
                // Ajouter seulement les champs nécessaires

                String token = Jwts.builder()
                        .claim("data", clientData) // Utiliser les données simplifiées
                        .signWith(SignatureAlgorithm.HS256, "SECRET")
                        .compact();

                response.put("token", token);
                response.put("user", clientData);


                return ResponseEntity.status(HttpStatus.OK).body(response);

                //Map pour Structure les données de réponse dans json

                // Reçoit email + mot de passe

                // Cherche le développeur en base

                // Si non trouvé → erreur 404

                //Si trouvé → vérifie le mot de passe

                // Si mot de passe incorrect → erreur 404

                // Si tout est bon → génère et retourne un JWT

                //JWT Contient les infos du développeur

                //Permettra l'authentification sur les requêtes suivantes

                // Réponses : Toujours au même format (Map)


            }

        }
    }




    @GetMapping("/disponibles")
    public ResponseEntity<List<Developpeur>> getDeveloppeursDisponibles() {
        List<Developpeur> devs = developpeurService.findDeveloppeursDisponibles();
        return ResponseEntity.ok(devs);
    }
    @PutMapping("/{id}/disponibilite")
    public ResponseEntity<?> updateDisponibilite(@PathVariable Long id, @RequestBody boolean disponible) {
        developpeurService.updateDisponibilite(id, disponible);
        return ResponseEntity.ok().build();
    }

    // Endpoint pour changer la disponibilité d'un développeur



    @PostMapping("/{equipeId}/membres/{developpeurId}")
    public ResponseEntity<?> ajouterMembre(@PathVariable Long equipeId, @PathVariable Long developpeurId) {
        Equipe equipe = equipeRepository.findById(equipeId).orElse(null);
        Developpeur dev = developpeurRepository.findById(developpeurId).orElse(null);

        if (equipe == null || dev == null) {
            return ResponseEntity.notFound().build();
        }

        dev.setDisponibilite(false);
        equipe.getMembres().add(dev);
        equipeRepository.save(equipe);
        developpeurRepository.save(dev);

        return ResponseEntity.ok("Membre ajouté avec succès");
    }
    @DeleteMapping("/{equipeId}/membres/{developpeurId}")
    public ResponseEntity<?> retirerMembre(@PathVariable Long equipeId, @PathVariable Long developpeurId) {
        Equipe equipe = equipeRepository.findById(equipeId).orElse(null);
        Developpeur dev = developpeurRepository.findById(developpeurId).orElse(null);

        if (equipe == null || dev == null) {
            return ResponseEntity.notFound().build();
        }

        equipe.getMembres().remove(dev);
        dev.setDisponibilite(true);
        equipeRepository.save(equipe);
        developpeurRepository.save(dev);

        return ResponseEntity.ok("Membre retiré avec succès");
    }






}