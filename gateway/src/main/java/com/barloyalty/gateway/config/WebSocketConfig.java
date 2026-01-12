package com.barloyalty.gateway.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@EnableWebSocketMessageBroker // Activează funcționalitatea de WebSocket
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    @Override
    public void configureMessageBroker(MessageBrokerRegistry config) {
        // Acesta este prefixul pentru canalele la care clienții se vor abona (asculta)
        // Ex: /topic/points/client1
        config.enableSimpleBroker("/topic");
        // Acesta este prefixul pentru mesajele trimise de la client la server (nu folosim în acest proiect)
        config.setApplicationDestinationPrefixes("/app");
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        // Înregistrăm endpoint-ul simplu pentru Postman/Testare RAW
        registry.addEndpoint("/ws")
                .setAllowedOriginPatterns("*"); // FĂRĂ .withSockJS() aici pentru Postman

        // Dacă ai nevoie de SockJS pentru un frontend în viitor, îl poți pune pe alt path:
        registry.addEndpoint("/ws-web")
                .setAllowedOriginPatterns("*")
                .withSockJS();
    }
}