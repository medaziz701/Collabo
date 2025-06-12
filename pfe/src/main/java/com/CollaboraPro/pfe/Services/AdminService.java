package com.CollaboraPro.pfe.Services;

import com.CollaboraPro.pfe.Entity.Admin;

import java.util.List;
import java.util.Optional;

public interface AdminService {
    Admin ajouterAdmin(Admin admin);
    Admin modifierAdmin(Admin admin);
    void supprimerAdmin(Long id);
    List<Admin> afficherAdmin();
    Optional<Admin> afficherAdminById(Long id);//hedhi bch ijibli bel id mouch kima list yaffichi les admin lkol
}
