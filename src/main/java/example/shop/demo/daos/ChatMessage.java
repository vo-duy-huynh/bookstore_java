package example.shop.demo.daos;

import example.shop.demo.constants.MessageType;
import lombok.Data;

@Data
public class ChatMessage {
    private MessageType type;
    private String content;
    private String sender;
}
