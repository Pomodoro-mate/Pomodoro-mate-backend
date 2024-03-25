package com.pomodoro.pomodoromate.websocket;

import com.auth0.jwt.exceptions.JWTDecodeException;
import com.pomodoro.pomodoromate.auth.exceptions.AuthenticationError;
import com.pomodoro.pomodoromate.auth.utils.JwtUtil;
import com.pomodoro.pomodoromate.common.models.SessionId;
import com.pomodoro.pomodoromate.participant.exceptions.ParticipantNotFoundException;
import com.pomodoro.pomodoromate.participant.models.Participant;
import com.pomodoro.pomodoromate.participant.repositories.ParticipantRepository;
import com.pomodoro.pomodoromate.studyRoom.exceptions.StudyRoomNotFoundException;
import com.pomodoro.pomodoromate.studyRoom.models.StudyRoom;
import com.pomodoro.pomodoromate.studyRoom.models.StudyRoomId;
import com.pomodoro.pomodoromate.studyRoom.repositories.StudyRoomRepository;
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
    private static final String AUTHORIZATION_HEADER = "Authorization";
    private static final String STUDY_ROOM_ID_HEADER = "studyRoomId";

    private final JwtUtil jwtUtil;
    private final ParticipantRepository participantRepository;
    private final StudyRoomRepository studyRoomRepository;

    public WebSocketHandler(JwtUtil jwtUtil,
                            ParticipantRepository participantRepository,
                            StudyRoomRepository studyRoomRepository) {
        this.jwtUtil = jwtUtil;
        this.participantRepository = participantRepository;
        this.studyRoomRepository = studyRoomRepository;
    }

    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(message);

        if(accessor.getCommand() == StompCommand.SUBSCRIBE) {
            handleSubscribeMessage(accessor);
        }

        if (accessor.getCommand() == StompCommand.DISCONNECT) {
            handleDisconnectMessage(accessor);
        }

        return message;
    }

    private void handleSubscribeMessage(StompHeaderAccessor accessor) {
        log.info("[web socket] - preSend 메서드 /connect / 시작");

        String authorization = accessor.getFirstNativeHeader(AUTHORIZATION_HEADER);
        log.info("authorization: " + authorization);

        if (authorization == null || !authorization.startsWith("Bearer ")) {
            throw new AuthenticationError();
        }

        String accessToken = authorization.substring("Bearer ".length());

        try {
            UserId userId = jwtUtil.decode(accessToken);
            log.info("userId: " + userId.value());

            Long studyRoomId = Long.valueOf(accessor.getFirstNativeHeader(STUDY_ROOM_ID_HEADER));
            log.info("studyRoomId: " + studyRoomId);

            StudyRoom studyRoom = studyRoomRepository.findById(studyRoomId)
                    .orElseThrow(StudyRoomNotFoundException::new);

            studyRoom.validateIncomplete();

            Map<String, Object> attributes = accessor.getSessionAttributes();
            attributes.put("UserId", userId);
            accessor.setSessionAttributes(attributes);

            String sessionId = accessor.getSessionId();
            log.info("sessionId: " + sessionId);

            Participant participant = participantRepository.findBy(userId, StudyRoomId.of(studyRoomId))
                    .orElseThrow(ParticipantNotFoundException::new);

            participant.activate(SessionId.of(sessionId));

            log.info("[web socket] - preSend 메서드 / connect / 완료");
        } catch (JWTDecodeException exception) {
            throw new AuthenticationError();
        }
    }

    private void handleDisconnectMessage(StompHeaderAccessor accessor) {
        log.info("[web socket] - preSend 메서드 / disconnect / 시작");

        String sessionId = accessor.getSessionId();
        log.info("sessionId: " + sessionId);

        Participant participant = participantRepository.findBySessionId(sessionId)
                .orElseThrow(ParticipantNotFoundException::new);

        participant.delete();

        log.info("[web socket] - preSend 메서드 / disconnect / 끝");
    }
}
