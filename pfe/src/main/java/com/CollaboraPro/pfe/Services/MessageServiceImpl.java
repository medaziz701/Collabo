package com.CollaboraPro.pfe.Services;

import com.CollaboraPro.pfe.Entity.Message;
import com.CollaboraPro.pfe.Repository.MessageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MessageServiceImpl implements MessageService {
    @Autowired
    MessageRepository messageRepository;


    @Override
    public Message envoyerMessage(Message message) {
        return messageRepository.save(message);
    }

    @Override
    public List<Message> getMessagesByProjet(Long projetId) {
        return messageRepository.findByProjetIdOrderByDateEnvoiAsc(projetId);
    }
}