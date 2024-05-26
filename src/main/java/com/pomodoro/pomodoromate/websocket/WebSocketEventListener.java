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
import org.springframework.scheduling.TaskScheduler;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

import java.util.Date;

@Slf4j
@Component
public class WebSocketEventListener {
    private static final String STUDY_ROOM_ID_HEADER = "StudyRoomId";
    private static final String PARTICIPATE_ID_HEADER = "ParticipantId";

    private final SimpMessagingTemplate messagingTemplate;
    private final GetParticipantsService getParticipantsService;
    private final ParticipantRepository participantRepository;
    private final CompleteStudyRoomService completeStudyRoomService;
    private final TaskScheduler taskScheduler;

    public WebSocketEventListener(SimpMessagingTemplate messagingTemplate,
                                  GetParticipantsService getParticipantsService,
                                  ParticipantRepository participantRepository,
                                  CompleteStudyRoomService completeStudyRoomService,
                                  TaskScheduler taskScheduler) {
        this.messagingTemplate = messagingTemplate;
        this.getParticipantsService = getParticipantsService;
        this.participantRepository = participantRepository;
        this.completeStudyRoomService = completeStudyRoomService;
        this.taskScheduler = taskScheduler;
    }

    @EventListener
    public void handleWebSocketDisconnectListener(SessionDisconnectEvent event) {
        log.info("[web socket] - handleWebSocketDisconnectListener 메서드 / 시작");
        StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(event.getMessage());

        Long participantId = (Long) headerAccessor.getSessionAttributes().get(PARTICIPATE_ID_HEADER);
        Long studyRoomId = (Long) headerAccessor.getSessionAttributes().get(STUDY_ROOM_ID_HEADER);

        log.info("participantId: {}", participantId);
        log.info("studyRoomId: {}", studyRoomId);

        if (participantId == null || studyRoomId == null) {
            log.warn("ParticipantId or StudyRoomId is missing in session attributes");
            return;
        }

        handleParticipantDisconnect(participantId, studyRoomId);

        log.info("[web socket] - handleWebSocketDisconnectListener 메서드 / 끝");
    }

    private void handleParticipantDisconnect(Long participantId, Long studyRoomId) {
        Participant participant = participantRepository.findById(participantId)
                .orElseThrow(ParticipantNotFoundException::new);

        if (participant.isDeleted()) {
            log.info("Participant {} is already deleted", participant.id().value());
            return;
        }

        participant.pend();
        participantRepository.save(participant);
        log.info("participant {} 상태를 PENDING 으로 변경", participant.id().value());

        schedulePendingParticipantCheck(participantId, studyRoomId);
    }

    private void schedulePendingParticipantCheck(Long participantId, Long studyRoomId) {
        taskScheduler.schedule(() -> checkPendingParticipantAndDelete(participantId, studyRoomId),
                new Date(System.currentTimeMillis() + 10000));
    }

    private void checkPendingParticipantAndDelete(Long participantId, Long studyRoomId) {
        log.info("[web socket] - CheckPendingParticipantAndDelete 메서드 / 시작");

        Participant participant = participantRepository.findById(participantId)
                .orElseThrow(ParticipantNotFoundException::new);

        if (participant.isPending()) {
            participant.delete();
            participantRepository.save(participant);
            log.info("participant {} 상태를 DELETED 로 변경", participant.id().value());

            Long participantCount = participantRepository.countNotDeletedBy(StudyRoomId.of(studyRoomId));

            if (participantCount == 0) {
                completeStudyRoomService.completeStudy(StudyRoomId.of(studyRoomId));
                log.info("studyRoom {} 스터디 종료", studyRoomId);

                log.info("[web socket] - CheckPendingParticipantAndDelete 메서드 / 끝");
                return;
            }
        }

        ParticipantSummariesDto participantSummariesDto = getParticipantsService
                .activeParticipants(StudyRoomId.of(studyRoomId));

        messagingTemplate.convertAndSend("/sub/studyrooms/" + studyRoomId + "/participants"
                , participantSummariesDto);

        log.info("[web socket] - CheckPendingParticipantAndDelete 메서드 / 끝");
    }
}
