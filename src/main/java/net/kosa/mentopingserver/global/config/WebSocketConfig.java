//package net.kosa.mentopingserver.global.config;
//
//
//import net.kosa.mentopingserver.global.handler.ChatWebSocketHandler;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.web.socket.config.annotation.EnableWebSocket;
//import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
//import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;
//
//@Configuration
//@EnableWebSocket
//public class WebSocketConfig implements WebSocketConfigurer {
//
//    private final ChatWebSocketHandler chatWebSocketHandler;
//
//    public WebSocketConfig(ChatWebSocketHandler chatWebSocketHandler) {
//        this.chatWebSocketHandler = chatWebSocketHandler;
//    }
//
//    @Override
//    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
//        registry.addHandler(chatWebSocketHandler, "/chat")
//                .setAllowedOrigins("*"); // CORS 설정
//    }
//
//}