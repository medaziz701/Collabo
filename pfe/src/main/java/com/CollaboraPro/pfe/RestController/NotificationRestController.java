package com.CollaboraPro.pfe.RestController;


import com.CollaboraPro.pfe.Entity.Notification;
import com.CollaboraPro.pfe.Services.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin("*")
@RequestMapping("/notifications")
public class NotificationRestController {
    @Autowired
     NotificationService notificationService;

    @GetMapping
    public List<Notification> getNotificationsAdmin() {
        return notificationService.getNotificationsPourAdmin(1L); // ID admin fixe
    }

    @PutMapping("/{id}/marquer-lue")
    public ResponseEntity<Void> marquerCommeLue(@PathVariable Long id) {
        notificationService.marquerCommeLue(id);
        return ResponseEntity.ok().build();
    }



}
