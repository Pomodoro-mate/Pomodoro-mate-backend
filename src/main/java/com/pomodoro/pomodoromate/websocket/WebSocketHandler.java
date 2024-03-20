package com.pomodoro.pomodoromate.websocket;

import com.auth0.jwt.exceptions.JWTDecodeException;
import com.pomodoro.pomodoromate.auth.exceptions.AuthenticationError;
import com.pomodoro.pomodoromate.auth.utils.JwtUtil;
import com.pomodoro.pomodoromate.user.models.UserId;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.stereotype.Component;

import java.util.Map;

@Slf4j
@Component
public class WebSocketHandler implements ChannelInterceptor {
    private JwtUtil jwtUtil;

    public WebSocketHandler(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(message);

        if(accessor.getCommand() == StompCommand.SUBSCRIBE) {
            log.info("[web socket] - preSend 메서드 /connect / 시작");

            String authorization = accessor.getFirstNativeHeader("Authorization");
            log.info("authorization: " + authorization);

            if (authorization == null || !authorization.startsWith("Bearer ")) {
                throw new AuthenticationError();
            }

            String accessToken = authorization.substring("Bearer ".length());

            try {
                UserId userId = jwtUtil.decode(accessToken);
                log.info("userId: " + userId.value());

                Map<String, Object> attributes = accessor.getSessionAttributes();
                attributes.put("UserId", userId);
                accessor.setSessionAttributes(attributes);

                log.info("[web socket] - preSend 메서드 / connect / 완료");

                return message;
            } catch (JWTDecodeException exception) {
                throw new AuthenticationError();
            }
        }

        return message;
    }
}
