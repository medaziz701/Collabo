package com.CollaboraPro.pfe.Services;


import com.CollaboraPro.pfe.Entity.Admin;
import com.CollaboraPro.pfe.Entity.Client;
import com.CollaboraPro.pfe.Entity.Notification;
import com.CollaboraPro.pfe.Entity.NotificationType;
import com.CollaboraPro.pfe.Repository.NotificationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class NotificationServiceImpl implements NotificationService{

    @Autowired
    private NotificationRepository notificationRepository;

    public void creerNotification(Admin admin, NotificationType titre, String contenu) {
        Notification notification = new Notification();
        notification.setTitre(titre);
        notification.setContenu(contenu);
        notification.setAdmin(admin);
        notificationRepository.save(notification);
    }




    public List<Notification> getNotificationsPourAdmin(Long adminId) {
        return notificationRepository.findByAdminIdOrderByDateCreationDesc(adminId);
    }
    public void marquerCommeLue(Long notificationId) {
        Notification notification = notificationRepository.findById(notificationId)
                .orElseThrow(() -> new RuntimeException("Notification non trouvée"));

        notification.setEstLue(true);
        notificationRepository.save(notification);
    }
}





