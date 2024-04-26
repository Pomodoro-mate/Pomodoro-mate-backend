package com.pomodoro.pomodoromate.websocket;

import com.pomodoro.pomodoromate.auth.exceptions.AccessTokenExpiredException;
import com.pomodoro.pomodoromate.auth.exceptions.TokenDecodingFailedException;
import com.pomodoro.pomodoromate.common.exceptions.CustomizedException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.Message;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.StompSubProtocolErrorHandler;

import java.nio.charset.StandardCharsets;

@Slf4j
@Component
public class WebSocketErrorHandler extends StompSubProtocolErrorHandler {
    public WebSocketErrorHandler() {
        super();
    }

    @Override
    public Message<byte[]> handleClientMessageProcessingError(Message<byte[]> clientMessage, Throwable exception) {
        log.info("[web socket error handler] - handleClientMessageProcessingError 메서드");

        log.info("exception: " + exception.getMessage());

        if (exception.getCause().getMessage().equals(new AccessTokenExpiredException().message())) {
            log.info("예외 내용 = {}", exception.getMessage());
            return handleCustomizedException(new AccessTokenExpiredException());
        }

        if (exception.getCause().getMessage().equals(new TokenDecodingFailedException().message())) {
            log.info("예외 내용 = {}", exception.getMessage());
            return handleCustomizedException(new TokenDecodingFailedException());
        }

        log.info("예외 내용 = {}", exception.getMessage());
        return super.handleClientMessageProcessingError(clientMessage, exception);
    }

    private Message<byte[]> handleCustomizedException(CustomizedException exception) {
        String message = exception.getMessage();
        log.info("exception message: " + exception.getMessage());
        log.info("exception code: " + exception.statusCode().value());

        StompHeaderAccessor accessor = StompHeaderAccessor.create(StompCommand.ERROR);

        accessor.setMessage(String.valueOf(exception.statusCode().value()));
        accessor.setLeaveMutable(true);

        return MessageBuilder.createMessage(message.getBytes(StandardCharsets.UTF_8), accessor.getMessageHeaders());
    }
}
