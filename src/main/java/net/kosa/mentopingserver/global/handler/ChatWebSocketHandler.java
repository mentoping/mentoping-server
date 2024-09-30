package net.kosa.mentopingserver.global.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import net.kosa.mentopingserver.domain.chat.ChatMessage;
import net.kosa.mentopingserver.domain.chat.ChatService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;

@Component
public class ChatWebSocketHandler extends TextWebSocketHandler {

    private static final Logger logger = LoggerFactory.getLogger(ChatWebSocketHandler.class);
    private static Map<String, WebSocketSession> sessions = new HashMap<>();

    private final ChatService chatService;
    private final ObjectMapper objectMapper = new ObjectMapper();

    public ChatWebSocketHandler(ChatService chatService) {
        this.chatService = chatService;
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        String userId = getUserId(session);
        if (userId != null) {
            sessions.put(userId, session);
            logger.info("WebSocket connection established for user: " + userId);
        } else {
            logger.warn("User ID is null. Connection not added to sessions.");
        }
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        String payload = message.getPayload();
        logger.info("Received message: " + payload);

        try {
            ChatMessage chatMessage = objectMapper.readValue(payload, ChatMessage.class);

            // 채팅 방 ID가 ChatMessage에 있다고 가정
            String chatId = chatMessage.getChatId();

            // 메시지 저장
            chatService.saveMessage(chatId, chatMessage);
            logger.info("Message saved to database. Chat ID: " + chatId);

            // 상대방에게 메시지 전달
            String receiverId = chatMessage.getReceiverId(); // 필드 이름이 receiverId로 통일되었다고 가정
            WebSocketSession receiverSession = sessions.get(receiverId);
            if (receiverSession != null && receiverSession.isOpen()) {
                receiverSession.sendMessage(new TextMessage(payload));
                logger.info("Message sent to recipient: " + receiverId);
            } else {
                logger.warn("Recipient session not found or closed: " + receiverId);
            }
        } catch (Exception e) {
            logger.error("Error processing message: ", e);
        }
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        String userId = getUserId(session);
        if (userId != null) {
            sessions.remove(userId);
            logger.info("WebSocket connection closed for user: " + userId);
        }
    }

    private String getUserId(WebSocketSession session) {
        URI uri = session.getUri();
        String query = uri.getQuery(); // 예: "userId=user1"
        if (query != null) {
            String[] params = query.split("&");
            for (String param : params) {
                String[] keyValue = param.split("=");
                if (keyValue.length == 2 && "userId".equals(keyValue[0])) {
                    return keyValue[1];
                }
            }
        }
        return null;
    }
}
