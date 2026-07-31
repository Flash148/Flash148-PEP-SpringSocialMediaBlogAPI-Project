package com.example.service;
import java.util.List;
import java.util.Optional;

import com.example.entity.Message;
import com.example.repository.AccountRepository;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;

import com.example.exception.InvalidMessageException;
import com.example.repository.MessageRepository;

@Service
public class MessageService {
    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private MessageRepository messageRepository;

     public Message createMessage(Message message) {
        if (message.getMessageText() == null || message.getMessageText().isBlank()) {
            throw new InvalidMessageException("Message text cannot be blank");
        }
        if (message.getMessageText().length() > 255) {
            throw new InvalidMessageException("Message text cannot exceed 255 characters");
        }
        if (message.getPostedBy() == null || !accountRepository.existsById(message.getPostedBy())) {
            throw new InvalidMessageException("Account does not exist");
        }
        return messageRepository.save(message);
    }

    public List<Message> getAllMessages() {
        return messageRepository.findAll();
    }
    
    public Optional<Message> getMessageById(Integer id) {
        return messageRepository.findById(id);
    }

    public int deleteMessage(Integer id) {
        if (!messageRepository.existsById(id)) {
            return 0;
        } else {
            messageRepository.deleteById(id);
            return 1;
        }
    }

    public int updateMessage(Integer id, String newText) {
        Message message = messageRepository.findById(id).orElseThrow(() -> new InvalidMessageException("Message id does not exist"));
        if (newText == null || newText.isBlank()) {
            throw new InvalidMessageException("Message cannot be blank");
        }
        if (newText.length() > 255) {
            throw new InvalidMessageException("Message text cannot exceed 255 characters");
        }
        message.setMessageText(newText);
        messageRepository.save(message);
        return 1;
    }

    public List<Message> getMessagesByPostedBy(Integer accountId) {
        return messageRepository.findByPostedBy(accountId);
    }
}
