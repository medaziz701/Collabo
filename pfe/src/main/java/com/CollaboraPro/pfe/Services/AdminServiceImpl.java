package com.CollaboraPro.pfe.Services;

import com.CollaboraPro.pfe.Entity.Admin;
import com.CollaboraPro.pfe.Repository.AdminRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AdminServiceImpl implements AdminService{
    @Autowired
    AdminRepository adminRepository;

    public Admin ajouterAdmin(Admin admin){

        return adminRepository.save(admin);
    }

    @Override
    public Admin modifierAdmin(Admin admin) {

        return adminRepository.save(admin);
    }



    public void supprimerAdmin(Long id){

        adminRepository.deleteById(id);
    }

    @Override
    public List<Admin> afficherAdmin() {

        return adminRepository.findAll();
    }

    @Override
    public Optional<Admin> afficherAdminById(Long id) {

        return adminRepository.findById(id);
    }
}
