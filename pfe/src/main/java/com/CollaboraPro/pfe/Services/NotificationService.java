package com.CollaboraPro.pfe.Services;

import com.CollaboraPro.pfe.Entity.Admin;
import com.CollaboraPro.pfe.Entity.Client;
import com.CollaboraPro.pfe.Entity.Notification;
import com.CollaboraPro.pfe.Entity.NotificationType;

import java.util.List;

public interface NotificationService {

    void creerNotification(Admin admin, NotificationType titre, String contenu);
    List<Notification> getNotificationsPourAdmin(Long adminId);
    void marquerCommeLue(Long notificationId);


}
