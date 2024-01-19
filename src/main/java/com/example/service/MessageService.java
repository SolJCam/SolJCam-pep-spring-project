package com.example.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.*;

import com.example.repository.MessageRepository;
import com.example.repository.AccountRepository;
import com.example.entity.Account;
import com.example.entity.Message;

@Service
public class MessageService {
    @Autowired
    MessageRepository messageRepository;
    @Autowired
    AccountRepository accountRepository;

    public MessageService(MessageRepository messageRepository, AccountRepository accountRepository){
        this.messageRepository = messageRepository;
        this.accountRepository = accountRepository;
    }

    public Message newMessage(String messageText, int postedBy, long timePosted){
        // check if the message is blank or is not over 255 characters
        if(messageText == "" || messageText.length() > 255){
            return null;
        }
        // save and return copy of Message if posted user exists
        if(accountRepository.accountIdExists(postedBy) instanceof Account){
            Message newMessage = new Message(postedBy, messageText, timePosted);
            return messageRepository.save(newMessage);
        }
        return null;
    }

    public List<Message> allMessages(){
        return messageRepository.findAll();
    }

    public Message messageById(int message_id){
        Optional<Message> optionalMessage = messageRepository.findById(message_id);
        if(optionalMessage.isPresent()){
            Message message = optionalMessage.get();
            return message;
        }
        return null;
    }

    @Transactional
    public int deleteMessageById(int message_id) throws Exception{
        messageRepository.deleteById(message_id);
        return 1;
    }

    @Transactional
    public int updateMessage(String updatedMessage, int message_id){
        if(updatedMessage.length() < 1 || updatedMessage.length() > 255){
            return 0;
        }
        Optional<Message> optionalMessage = messageRepository.findById(message_id);
        if(optionalMessage.isEmpty()){
            return 0;
        }
        Message message = optionalMessage.get();
        message.setMessage_text(updatedMessage);
        List<Message> messageList = new ArrayList<>();
        messageList.add(messageRepository.save(message));
        return messageList.size();
    }

    public List<Message> allUserMessages(int userId){
        return messageRepository.findPostedBy(userId);
    }
}
