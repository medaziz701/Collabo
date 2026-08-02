package com.CollaboraPro.pfe.RestController;
import com.CollaboraPro.pfe.Entity.Admin;
import com.CollaboraPro.pfe.Entity.ChefEquipe;
import com.CollaboraPro.pfe.Entity.Client;
import com.CollaboraPro.pfe.Entity.NotificationType;
import com.CollaboraPro.pfe.Repository.ClientRepository;
import com.CollaboraPro.pfe.Services.ClientService;
import com.CollaboraPro.pfe.Services.EmailDService;
import com.CollaboraPro.pfe.Services.NotificationService;
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

@RestController// Indique que c'est un contrôleur Spring avec des réponses au format JSON
@CrossOrigin("*")// Autorise les requêtes CORS depuis n'importe quelle origine
@RequestMapping(value = "/client")//Toutes les routes de ce contrôleur commenceront par /client
public class ClientRestController {
    private BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();//Crée une instance de BCrypt pour hasher les mots de passe



    @Autowired
    ClientRepository clientRepository;

    @Autowired
    ClientService clientService;

    @Autowired
    NotificationService notificationService;
    @Autowired
    private EmailDService emailDService;

    @RequestMapping(method = RequestMethod.POST)
    ResponseEntity<?> AjouterClient(@RequestBody Client client) {
        HashMap<String, Object> response = new HashMap<>();
        if(clientRepository.existsByEmail(client.getEmail())) {
            response.put("message", "email exist deja !");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        } else {
            
            // Sauvegarde du mot de passe en clair temporairement pour l'email
            String plainPassword = client.getMp();
            Admin admin = new Admin();
            admin.setId(1L);
            client.setManagedByAdmin(admin);
            notificationService.creerNotification(
                    admin,
                    NotificationType.NOUVEAU_CLIENT,
                    " Un nouveau client nommé " + client.getNom() + " est inscrit"


            );
            // Hash et sauvegarde du client
            client.setMp(this.bCryptPasswordEncoder.encode(plainPassword));
            Client savedUser = clientRepository.save(client);

            // Envoi de l'email


            return ResponseEntity.status(HttpStatus.CREATED).body(savedUser);
        }
    }




    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    public Client modifierclient(@PathVariable("id") Long id, @RequestBody Client client) {
        return clientRepository.findById(id).map(existingClient -> {
            // Mise à jour des champs de base
            existingClient.setNom(client.getNom());
            existingClient.setPrenom(client.getPrenom());
            existingClient.setTlf(client.getTlf());
            existingClient.setEmail(client.getEmail());
            existingClient.setAdresse(client.getAdresse());
            existingClient.setGenre(client.getGenre());
            existingClient.setCin(client.getCin());

            // Gestion sécurisée du mot de passe
            if (client.getMp() != null && !client.getMp().isEmpty()
                    && !bCryptPasswordEncoder.matches(client.getMp(), existingClient.getMp())) {
                existingClient.setMp(bCryptPasswordEncoder.encode(client.getMp()));
            }

            // Gestion de l'état avec notification
            if (client.isEtat() != existingClient.isEtat()) {
                String etat = client.isEtat() ? "Accepté" : "Bloqué";
                emailDService.SendSimpleMessage(existingClient.getEmail(),
                        "État de votre compte",
                        "Votre compte a été " + etat);
                existingClient.setEtat(client.isEtat());
            }

            return clientRepository.save(existingClient);
        }).orElseThrow(() -> new RuntimeException("Client non trouvé"));
    }



    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE )
    public void suppClient(@PathVariable("id") Long id){
        clientService.supprimerClient(id);

    }



    @RequestMapping(method = RequestMethod.GET )
    public List<Client> afficherClient(){
        return clientService.afficherClient();

    }


    @PostMapping("/login")
    public ResponseEntity<Map<String, Object>> loginClient(@RequestBody Client client) {
        System.out.println("in login-client: " + client);
        HashMap<String, Object> response = new HashMap<>();

        Client userFromDB = clientRepository.findClientByEmail(client.getEmail());
        System.out.println("userFromDB: " + userFromDB);

        if (userFromDB == null) {
            response.put("message", "Client not found !");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        } else {
            // Vérification de l'état du compte
            if (!userFromDB.isEtat()) {
                response.put("message", "Votre compte n'est pas encore activé par l'administrateur");
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
            }

            // Vérification du mot de passe
            boolean passwordMatch = this.bCryptPasswordEncoder.matches(client.getMp(), userFromDB.getMp());
            System.out.println("passwordMatch: " + passwordMatch);

            if (!passwordMatch) {
                response.put("message", "Client not found !"); // Même message que pour email incorrect (sécurité)
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
            } else {
                // Construction des données pour le token
                Map<String, Object> clientData = new HashMap<>();
                clientData.put("id", userFromDB.getId());
                clientData.put("email", userFromDB.getEmail());
                clientData.put("nom", userFromDB.getNom());
                clientData.put("prenom", userFromDB.getPrenom());


                // Génération du token JWT
                String token = Jwts.builder()
                        .claim("data", clientData)
                        .signWith(SignatureAlgorithm.HS256, "SECRET")
                        .compact();

                response.put("token", token);


                return ResponseEntity.status(HttpStatus.OK).body(response);
            }
        }
    }

    @RequestMapping(value = "/{id}" , method = RequestMethod.GET)
    public Optional<Client> getClientById(@PathVariable("id") Long id){

        Optional<Client> client = clientService.afficherClientById(id);
        return client;// Retourne le client correspondant à l'ID
    }




    private static final String GOOGLE_TOKEN_URL = "https://oauth2.googleapis.com/tokeninfo?id_token=";

    @PostMapping("/login-google")
    public ResponseEntity<Map<String, Object>> loginWithGoogle(@RequestParam("id_token") String idToken) {
        Map<String, Object> response = new HashMap<>();
        try {
            String googleUserInfo = validateGoogleToken(idToken);
            JsonNode userInfo = new ObjectMapper().readTree(googleUserInfo);

            String email = userInfo.get("email").asText();
            String fullName = userInfo.get("name").asText();
            String firstName = fullName.split(" ")[0]; // Prenons le prénom comme étant la première partie du nom complet
            String lastName = fullName.split(" ").length > 1 ? fullName.split(" ")[1] : ""; // Nom de famille s'il existe

            Client existingClient = clientRepository.findClientByEmail(email);

            if (existingClient == null) {

                Client newClient = new Client();
                newClient.setEmail(email);
                newClient.setNom(lastName); // Nom
                newClient.setPrenom(firstName); // Prénom
                newClient.setMp("defaultPassword"); // Mot de passe temporaire, à changer plus tard
                newClient.setEtat(false);  // Statut actif

                clientRepository.save(newClient);
                existingClient = newClient;
            }

            String token = generateToken(existingClient);
            response.put("token", token);

            return ResponseEntity.ok(response);

        } catch (IOException e) {
            response.put("message", "Erreur lors du traitement du token Google : " + e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        } catch (Exception e) {
            response.put("message", "Une erreur inconnue est survenue.");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    private String validateGoogleToken(String idToken) {
        String url = GOOGLE_TOKEN_URL + idToken;
        RestTemplate restTemplate = new RestTemplate();
        return restTemplate.getForObject(url, String.class);
    }

    private String generateToken(Client client) {
        return Jwts.builder()
                .claim("data", client)
                .signWith(SignatureAlgorithm.HS256, "SECRET_KEY")
                .compact();
    }




}
