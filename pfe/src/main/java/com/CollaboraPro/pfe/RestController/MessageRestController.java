package com.CollaboraPro.pfe.RestController;

import com.CollaboraPro.pfe.Entity.Message;
import com.CollaboraPro.pfe.Services.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin("*")
@RequestMapping(value = "/messages")
public class MessageRestController {
    @Autowired
    MessageService messageService;



    @PostMapping
    public Message envoyerMessage(@RequestBody Message message) {
        return messageService.envoyerMessage(message);
    }

    @GetMapping("/projet/{projetId}")
    public List<Message> getMessagesByProjet(@PathVariable Long projetId) {
        return messageService.getMessagesByProjet(projetId);
    }
}