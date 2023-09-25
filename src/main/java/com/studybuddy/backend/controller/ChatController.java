package com.studybuddy.backend.controller;

import com.google.gson.Gson;
import com.studybuddy.backend.object.ChatMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
public class ChatController {
    @Autowired
    private Gson gson;

    @Autowired
    private SimpMessagingTemplate template;

    @MessageMapping("/chat/send")
//    @SendTo("/topic/group")
    public void broadcastGroupMessage(@Payload ChatMessage message) {
        //Sending this message to all the subscribers
        log.info("Message: " + gson.toJson(message));
        template.convertAndSend("/topic/question" + message.getQuestionId(), message);
    }

//    @MessageMapping("/chat/newUser")
//    @SendTo("/topic/group")
//    public ChatMessage addUser(@Payload ChatMessage message,
//                           SimpMessageHeaderAccessor headerAccessor) {
//        // Add user in web socket session
//        headerAccessor.getSessionAttributes().put("username", message.getSender());
//        return message;
//    }
}
