package com.pomodoro.pomodoromate.websocket.config;

import com.pomodoro.pomodoromate.websocket.WebSocketErrorHandler;
import com.pomodoro.pomodoromate.websocket.WebSocketHandler;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.ChannelRegistration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {
    @Value("${allow-origin}")
    private String[] allowOrigins;

    private final WebSocketHandler webSocketHandler;
    private final WebSocketErrorHandler webSocketErrorHandler;

    public WebSocketConfig(WebSocketHandler webSocketHandler, WebSocketErrorHandler webSocketErrorHandler) {
        this.webSocketHandler = webSocketHandler;
        this.webSocketErrorHandler = webSocketErrorHandler;
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/study")
                .setAllowedOrigins(allowOrigins)
                .withSockJS();

        registry.setErrorHandler(webSocketErrorHandler);
    }

    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        registry.enableSimpleBroker("/sub");
        registry.setApplicationDestinationPrefixes("/pub");
    }

    @Override
    public void configureClientInboundChannel(ChannelRegistration registration) {
        registration.interceptors(webSocketHandler);
    }
}
