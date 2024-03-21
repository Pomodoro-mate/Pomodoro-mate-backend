package com.pomodoro.pomodoromate.participant.cotrollers;

import com.pomodoro.pomodoromate.participant.applications.GetParticipantsService;
import com.pomodoro.pomodoromate.participant.applications.LeaveStudyService;
import com.pomodoro.pomodoromate.participant.applications.ParticipantSummariesDto;
import com.pomodoro.pomodoromate.participant.applications.ParticipateService;
import com.pomodoro.pomodoromate.participant.dtos.ParticipateResponseDto;
import com.pomodoro.pomodoromate.participant.models.ParticipantId;
import com.pomodoro.pomodoromate.studyRoom.models.StudyRoomId;
import com.pomodoro.pomodoromate.user.models.UserId;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("api")
public class ParticipantController {
    private final ParticipateService participateService;
    private final LeaveStudyService leaveStudyService;
    private final GetParticipantsService getParticipantsService;
    private final SimpMessagingTemplate messagingTemplate;

    public ParticipantController(ParticipateService participateService,
                                 LeaveStudyService leaveStudyService,
                                 GetParticipantsService getParticipantsService,
                                 SimpMessagingTemplate messagingTemplate) {
        this.participateService = participateService;
        this.leaveStudyService = leaveStudyService;
        this.getParticipantsService = getParticipantsService;
        this.messagingTemplate = messagingTemplate;
    }

    @Operation(summary = "스터디 참가")
    @ApiResponse(responseCode = "201")
    @PostMapping("studyrooms/{studyRoomId}/participants")
    public ResponseEntity<ParticipateResponseDto> participate(
            @RequestAttribute UserId userId,
            @PathVariable Long studyRoomId
    ) {
        Long participantId = participateService.participate(userId, StudyRoomId.of(studyRoomId));

        ParticipantSummariesDto participantSummariesDto = getParticipantsService
                .activeParticipants(StudyRoomId.of(studyRoomId));

        messagingTemplate.convertAndSend("/sub/studyrooms/" + studyRoomId + "/participants"
                , participantSummariesDto);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ParticipateResponseDto(participantId));
    }

    @Operation(summary = "스터디 나가기")
    @ApiResponse(responseCode = "204")
    @DeleteMapping("studyrooms/{studyRoomId}/participants/{participantId}")
    public ResponseEntity<Void> leaveStudy(
            @RequestAttribute UserId userId,
            @PathVariable Long studyRoomId,
            @PathVariable Long participantId
    ) {
        leaveStudyService.leaveStudy(userId, StudyRoomId.of(studyRoomId), ParticipantId.of(participantId));

        ParticipantSummariesDto participantSummariesDto = getParticipantsService
                .activeParticipants(StudyRoomId.of(studyRoomId));

        messagingTemplate.convertAndSend("/sub/studyrooms/" + studyRoomId + "/participants"
                , participantSummariesDto);

        return ResponseEntity.noContent().build();
    }
}
