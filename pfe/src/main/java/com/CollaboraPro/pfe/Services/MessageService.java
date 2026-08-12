package com.CollaboraPro.pfe.Services;

import com.CollaboraPro.pfe.Entity.Message;
import java.util.List;

public interface MessageService {
    Message envoyerMessage(Message message);
    List<Message> getMessagesByProjet(Long projetId);

}