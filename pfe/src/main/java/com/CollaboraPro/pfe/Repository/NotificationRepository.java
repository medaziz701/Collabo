package com.CollaboraPro.pfe.Repository;

import com.CollaboraPro.pfe.Entity.Notification;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NotificationRepository extends JpaRepository<Notification, Long> {
    List<Notification> findByAdminIdOrderByDateCreationDesc(Long adminId);


}
