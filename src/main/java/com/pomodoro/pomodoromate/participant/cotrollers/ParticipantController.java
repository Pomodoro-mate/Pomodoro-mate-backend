package com.pomodoro.pomodoromate.participant.cotrollers;

import com.pomodoro.pomodoromate.participant.applications.ParticipateService;
import com.pomodoro.pomodoromate.studyRoom.models.StudyRoomId;
import com.pomodoro.pomodoromate.user.models.UserId;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;

@RestController
public class ParticipantController {
    private final ParticipateService participateService;

    public ParticipantController(ParticipateService participateService) {
        this.participateService = participateService;
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
}
