package com.example.controller;


/**
 * TODO: You will need to write your own endpoints and handlers for your controller using Spring. The endpoints you will need can be
 * found in readme.md as well as the test cases. You be required to use the @GET/POST/PUT/DELETE/etc Mapping annotations
 * where applicable as well as the @ResponseBody and @PathVariable annotations. You should
 * refer to prior mini-project labs and lecture materials for guidance on how a controller may be built.
 */

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.entity.Account;
import com.example.entity.Message;
import com.example.exception.DuplicateUsernameException;
import com.example.exception.InvalidLoginException;
import com.example.exception.InvalidMessageException;
import com.example.exception.InvalidRegistrationException;
import com.example.service.AccountService;
import com.example.service.MessageService;

@RestController
public class SocialMediaController {
    @Autowired
    private AccountService accountService;

    @Autowired
    private MessageService messageService;

    @PostMapping("/register")
    public Account registerAccount(@RequestBody Account account) {
        return accountService.registerAccount(account.getUsername(), account.getPassword());
    }

    @PostMapping("/login")
    public Account login(@RequestBody Account account) {
        return accountService.login(account.getUsername(), account.getPassword());
    }

    @PostMapping("/messages")
    public Message createMessage(@RequestBody Message message) {
        return messageService.createMessage(message);
    }

    @GetMapping("/messages")
    public List<Message> getAllMessages() {
        return messageService.getAllMessages();
    }

    @GetMapping("/messages/{id}")
    public ResponseEntity<?> getMessageById(@PathVariable Integer id) {
        Optional<Message> result = messageService.getMessageById(id);
        if (result.isPresent()) {
            return ResponseEntity.ok(result.get());
        } else {
            return ResponseEntity.ok().build();
        }
    }

    @DeleteMapping("/messages/{id}")
    public ResponseEntity<?> deleteMessage(@PathVariable Integer id) {
        int rows = messageService.deleteMessage(id);
        if (rows == 0) {
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.ok(rows);
        }
    }

    @PatchMapping("/messages/{id}")
    public int updateMessage(@PathVariable Integer id, @RequestBody Message message) {
        return messageService.updateMessage(id, message.getMessageText());
    }

    @GetMapping("/accounts/{accountId}/messages")
    public List<Message> getMessagesByPostedBy(@PathVariable Integer accountId) {
        return messageService.getMessagesByPostedBy(accountId);
    }

    @ExceptionHandler(DuplicateUsernameException.class)
    public ResponseEntity<?> handleDuplicateUsername() {
        return ResponseEntity.status(409).build();
    }

    @ExceptionHandler(InvalidRegistrationException.class)
    public ResponseEntity<?> handleInvalidRegistration() {
        return ResponseEntity.status(400).build();
    }

    @ExceptionHandler(InvalidLoginException.class)
    public ResponseEntity<?> handleInvalidLogin() {
        return ResponseEntity.status(401).build();
    }

    @ExceptionHandler(InvalidMessageException.class)
    public ResponseEntity<?> handleInvalidMessage() {
        return ResponseEntity.status(400).build();
    }
} 
