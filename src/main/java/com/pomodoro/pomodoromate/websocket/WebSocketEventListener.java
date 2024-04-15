package com.pomodoro.pomodoromate.websocket;

import com.pomodoro.pomodoromate.participant.applications.GetParticipantsService;
import com.pomodoro.pomodoromate.participant.dtos.ParticipantSummariesDto;
import com.pomodoro.pomodoromate.participant.exceptions.ParticipantNotFoundException;
import com.pomodoro.pomodoromate.participant.models.Participant;
import com.pomodoro.pomodoromate.participant.repositories.ParticipantRepository;
import com.pomodoro.pomodoromate.studyRoom.applications.CompleteStudyRoomService;
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
    private static final String PARTICIPATE_ID_HEADER = "ParticipantId";

    private final SimpMessagingTemplate messagingTemplate;
    private final GetParticipantsService getParticipantsService;
    private final ParticipantRepository participantRepository;
    private final CompleteStudyRoomService completeStudyRoomService;

    public WebSocketEventListener(SimpMessagingTemplate messagingTemplate,
                                  GetParticipantsService getParticipantsService,
                                  ParticipantRepository participantRepository,
                                  CompleteStudyRoomService completeStudyRoomService) {
        this.messagingTemplate = messagingTemplate;
        this.getParticipantsService = getParticipantsService;
        this.participantRepository = participantRepository;
        this.completeStudyRoomService = completeStudyRoomService;
    }

    @EventListener
    public void handleWebSocketDisconnectListener(SessionDisconnectEvent event) {
        log.info("[web socket] - handleWebSocketDisconnectListener 메서드 / 시작");
        StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(event.getMessage());

        Long participantId = (Long) headerAccessor.getSessionAttributes().get(PARTICIPATE_ID_HEADER);
        log.info("participantId: " + participantId);

        Participant participant = participantRepository.findById(participantId)
                .orElseThrow(ParticipantNotFoundException::new);

        Long studyRoomId = (Long) headerAccessor.getSessionAttributes().get(STUDY_ROOM_ID_HEADER);
        log.info("studyRoomId: " + studyRoomId);

        if (participant != null && studyRoomId != null) {
            log.info("participant: " + participant.id().value() + " : " + participant.status().toString());

            participant.delete();

            participantRepository.save(participant);

            log.info("participant: " + participant.id().value() + " : " + participant.status().toString());

            Long participantCount = participantRepository.countActiveBy(StudyRoomId.of(studyRoomId));

            if (participantCount == 0) {
                completeStudyRoomService.completeStudy(StudyRoomId.of(studyRoomId));

                log.info("[web socket] - handleWebSocketDisconnectListener 메서드 / 끝");
                return;
            }

            ParticipantSummariesDto participantSummariesDto = getParticipantsService
                    .activeParticipants(StudyRoomId.of(studyRoomId));

            messagingTemplate.convertAndSend("/sub/studyrooms/" + studyRoomId + "/participants"
                    , participantSummariesDto);
        }

        log.info("[web socket] - handleWebSocketDisconnectListener 메서드 / 끝");
    }
}
