package example.shop.demo.controllers;

import example.shop.demo.daos.ChatMessage;
import jakarta.validation.constraints.NotNull;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@CrossOrigin("*")
@RequestMapping("/")
public class ChatController {

    @GetMapping("/chat")
    public String chat() {
        return "product/chat";
    }

    @MessageMapping("/chat.sendMessage")
    @SendTo("/topic/public")
    public ChatMessage sendMessage(@Payload ChatMessage chatMessage) {
        return chatMessage;
    }

    @MessageMapping("/chat.addUser")
    @SendTo("/topic/public")
    public ChatMessage addUser(@Payload @NotNull ChatMessage chatMessage,
                               @NotNull SimpMessageHeaderAccessor headerAccessor) {
        var sessionAttributes = headerAccessor.getSessionAttributes();
        if (sessionAttributes != null)
            sessionAttributes.put("username", chatMessage.getSender());
        return chatMessage;
    }
}
