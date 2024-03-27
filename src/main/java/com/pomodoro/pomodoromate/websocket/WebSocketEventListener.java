package com.pomodoro.pomodoromate.websocket;

import com.pomodoro.pomodoromate.participant.applications.GetParticipantsService;
import com.pomodoro.pomodoromate.participant.applications.ParticipantSummariesDto;
import com.pomodoro.pomodoromate.studyRoom.models.StudyRoomId;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

@Slf4j
@Component
public class WebSocketEventListener {
    private static final String STUDY_ROOM_ID_HEADER = "StudyRoomId";

    private final SimpMessagingTemplate messagingTemplate;
    private final GetParticipantsService getParticipantsService;

    public WebSocketEventListener(SimpMessagingTemplate messagingTemplate,
                                  GetParticipantsService getParticipantsService) {
        this.messagingTemplate = messagingTemplate;
        this.getParticipantsService = getParticipantsService;
    }

    @EventListener
    public void handleWebSocketDisconnectListener(SessionDisconnectEvent event) {
        log.info("[web socket] - handleWebSocketDisconnectListener 메서드 / 시작");
        StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(event.getMessage());

        Long studyRoomId = (Long) headerAccessor.getSessionAttributes().get(STUDY_ROOM_ID_HEADER);
        log.info("studyRoomId: " + studyRoomId);

        if (studyRoomId != null) {
            ParticipantSummariesDto participantSummariesDto = getParticipantsService
                    .activeParticipants(StudyRoomId.of(studyRoomId));

            messagingTemplate.convertAndSend("/sub/studyrooms/" + studyRoomId + "/participants"
                    , participantSummariesDto);
        }

        log.info("[web socket] - handleWebSocketDisconnectListener 메서드 / 끝");
    }
}
