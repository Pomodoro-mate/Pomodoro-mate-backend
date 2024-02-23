package com.pomodoro.pomodoromate.participant.cotrollers;

import com.pomodoro.pomodoromate.participant.applications.LeaveStudyService;
import com.pomodoro.pomodoromate.participant.applications.ParticipateService;
import com.pomodoro.pomodoromate.participant.models.ParticipantId;
import com.pomodoro.pomodoromate.studyRoom.models.StudyRoomId;
import com.pomodoro.pomodoromate.user.models.UserId;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;

@RestController
public class ParticipantController {
    private final ParticipateService participateService;
    private final LeaveStudyService leaveStudyService;

    public ParticipantController(ParticipateService participateService, LeaveStudyService leaveStudyService) {
        this.participateService = participateService;
        this.leaveStudyService = leaveStudyService;
    }

    @Operation(summary = "스터디 참가")
    @ApiResponse(responseCode = "201")
    @PostMapping("studyrooms/{studyRoomId}/participants")
    public ResponseEntity<Void> participate(
            @RequestAttribute UserId userId,
            @PathVariable Long studyRoomId
    ) {
        Long participantId = participateService.participate(userId, StudyRoomId.of(studyRoomId));

        return ResponseEntity.created(
                URI.create("/studyrooms/" + studyRoomId + "/participants/" + participantId)).build();
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

        return ResponseEntity.noContent().build();
    }
}
