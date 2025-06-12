package com.CollaboraPro.pfe.RestController;

import com.CollaboraPro.pfe.Entity.Admin;
import com.CollaboraPro.pfe.Repository.AdminRepository;
import com.CollaboraPro.pfe.Services.AdminService;
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
@RequestMapping(value = "/admin")
public class adminRestController {
    private BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();

    @Autowired
    AdminRepository adminRepository;

    @Autowired
    AdminService adminService;


    @RequestMapping(method = RequestMethod.POST )
    ResponseEntity<?> AjouterAdmin (@RequestBody Admin admin){
        HashMap<String, Object> response = new HashMap<>();
        if(adminRepository.existsByEmail(admin.getEmail())){
            response.put("message", "email exist deja !");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }else{
            admin.setMp(this.bCryptPasswordEncoder.encode(admin.getMp()));
            Admin savedUser = adminRepository.save(admin);
            return ResponseEntity.status(HttpStatus.CREATED).body(savedUser);}

    }



    @RequestMapping(value = "/{id}" ,method = RequestMethod.PUT)
    public Admin modifieradmin(@PathVariable("id")Long id, @RequestBody Admin admin){
        admin.setMp(this.bCryptPasswordEncoder.encode(admin.getMp()));
        Admin savedUser = adminRepository.save(admin);

        Admin newAdmin = adminService.modifierAdmin(admin);
        return newAdmin;
    }



    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE )
    public void suppAdmin(@PathVariable("id") Long id){
        adminService.supprimerAdmin(id);

    }



    @RequestMapping(method = RequestMethod.GET )
    public List<Admin> afficherAdmin(){
        return adminService.afficherAdmin();

    }


    @PostMapping("/login")
    public ResponseEntity<Map<String, Object>> loginAdmin(@RequestBody Admin admin) {
        System.out.println("in login-admin"+admin);
        HashMap<String, Object> response = new HashMap<>();

        Admin userFromDB = adminRepository.findAdminByEmail(admin.getEmail());
        System.out.println("userFromDB+admin"+userFromDB);
        if (userFromDB == null) {
            response.put("message", "Admin not found !");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        } else {
            boolean compare = this.bCryptPasswordEncoder.matches(admin.getMp(), userFromDB.getMp());
            System.out.println("compare"+compare);
            if (!compare) {
                response.put("message", "admin not found !");
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
            }else
            {
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
    public Optional<Admin> getAdminById(@PathVariable("id") Long id){

        Optional<Admin> admin = adminService.afficherAdminById(id);
        return admin;
    }
}
