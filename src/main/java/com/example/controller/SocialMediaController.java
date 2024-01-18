package com.example.controller;

import org.springframework.http.ResponseEntity;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.*;

import com.example.entity.Account;
import com.example.entity.Message;
import com.example.service.AccountService;
import com.example.service.MessageService;
import com.example.exception.*;

/**
 * TODO: You will need to write your own endpoints and handlers for your controller using Spring. The endpoints you will need can be
 * found in readme.md as well as the test cases. You be required to use the @GET/POST/PUT/DELETE/etc Mapping annotations
 * where applicable as well as the @ResponseBody and @PathVariable annotations. You should
 * refer to prior mini-project labs and lecture materials for guidance on how a controller may be built.
 */

@RestController
public class SocialMediaController {
    @Autowired
    AccountService accountService;
    @Autowired
    MessageService messageService;

    public SocialMediaController(AccountService accountService, MessageService messageService){
        this.accountService = accountService;
        this.messageService = messageService;
    }

    @PostMapping("/register")
    private ResponseEntity<Account> register(@RequestBody Account account) {
        Account accountRegistered = accountService.registerAccount(account.getUsername(), account.getPassword());
        if(accountRegistered == null){
            throw new RegistrationUnsuccessfulException();
        }else if(accountRegistered.getUsername().equals("")){
            throw new AccountAlreadyExistsException();
        }else{
            return ResponseEntity.status(200).body(accountRegistered);
        }
    }

    @PostMapping("/login")
    private ResponseEntity<Account> login(@RequestBody Account account) {
        Account accountSession = accountService.login(account.getUsername(), account.getPassword());
        if(accountSession == null){
            throw new UnauthorizedLoginException();
        }else{
            return ResponseEntity.status(200).body(accountSession);
        }
    }

    @PostMapping("/messages")
    private ResponseEntity<Message> postMessageHandler(@RequestBody Message message) {
        Message newMessage = messageService.newMessage(message.getMessage_text(), message.getPosted_by(), message.getTime_posted_epoch());
        if (newMessage == null) {
            throw new MessageSaveUnsuccessfulException();
        } else {
            return ResponseEntity.status(200).body(newMessage);
        }
    }

    @GetMapping("/messages")
    private ResponseEntity<List<Message>> getAllMessages() {
        List<Message> messageList = messageService.allMessages();
        return ResponseEntity.status(200).body(messageList);
    }

    @GetMapping("/messages/{message_id}")
    private ResponseEntity<Message> getMessageById(@PathVariable int message_id) {
        Message message = messageService.messageById(message_id);
        return ResponseEntity.status(200).body(message);
    }

    @DeleteMapping("/messages/{message_id}")
    private ResponseEntity<Integer> deleteMessageById(@PathVariable int message_id) {
        Integer deletedMessages;
        try {
            deletedMessages = messageService.deleteMessageById(message_id);
        } catch (Exception e) {
            deletedMessages = null;
        }
        return ResponseEntity.status(200).body(deletedMessages);
    }

    @RequestMapping(value = "/messages/{message_id}", produces = "application/json", method=RequestMethod.PATCH)
    private ResponseEntity<Integer> updateMessage(@RequestBody Message message, @PathVariable int message_id) {
        int updateSuccessful = messageService.updateMessage(message.getMessage_text(), message_id);
        if( updateSuccessful > 0){
            return ResponseEntity.status(200).body(updateSuccessful);
        }else{
            throw new MessageUpdateUnsuccessfulException();
        }
    }

    @GetMapping("/accounts/{account_id}/messages")
    private ResponseEntity<List<Message>> getAllMessagesByUserId(@PathVariable int account_id) {
        List<Message> allUserMessages = messageService.allUserMessages(account_id);
        return ResponseEntity.status(200).body(allUserMessages);
    }
}
