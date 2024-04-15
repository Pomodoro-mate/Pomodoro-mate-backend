package com.pomodoro.pomodoromate.studyRoom.controllers;

import com.pomodoro.pomodoromate.participant.applications.ParticipateService;
import com.pomodoro.pomodoromate.studyRoom.applications.CreateStudyRoomService;
import com.pomodoro.pomodoromate.studyRoom.applications.GetStudyRoomService;
import com.pomodoro.pomodoromate.studyRoom.applications.GetStudyRoomsService;
import com.pomodoro.pomodoromate.studyRoom.applications.StudyProgressService;
import com.pomodoro.pomodoromate.studyRoom.dtos.CreateStudyRoomRequest;
import com.pomodoro.pomodoromate.studyRoom.dtos.CreateStudyRoomRequestDto;
import com.pomodoro.pomodoromate.studyRoom.dtos.CreateStudyRoomResponseDto;
import com.pomodoro.pomodoromate.studyRoom.dtos.StudyProgressRequestDto;
import com.pomodoro.pomodoromate.studyRoom.dtos.StudyRoomDetailDto;
import com.pomodoro.pomodoromate.studyRoom.dtos.StudyRoomSummariesDto;
import com.pomodoro.pomodoromate.studyRoom.models.Step;
import com.pomodoro.pomodoromate.studyRoom.models.StudyRoomId;
import com.pomodoro.pomodoromate.user.models.UserId;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "스터디룸 API")
@RestController
public class StudyRoomController {
    private final CreateStudyRoomService createStudyRoomService;
    private final GetStudyRoomsService getStudyRoomsService;
    private final GetStudyRoomService getStudyRoomService;
    private final StudyProgressService studyProgressService;
    private final ParticipateService participateService;

    public StudyRoomController(CreateStudyRoomService createStudyRoomService,
                               GetStudyRoomsService getStudyRoomsService,
                               GetStudyRoomService getStudyRoomService,
                               StudyProgressService studyProgressService,
                               ParticipateService participateService) {
        this.createStudyRoomService = createStudyRoomService;
        this.getStudyRoomsService = getStudyRoomsService;
        this.getStudyRoomService = getStudyRoomService;
        this.studyProgressService = studyProgressService;
        this.participateService = participateService;
    }

    @Operation(summary = "스터디룸 목록 조회")
    @GetMapping("api/studyrooms")
    public ResponseEntity<StudyRoomSummariesDto> studyRooms(
            @RequestAttribute UserId userId,
            @RequestParam(required = false, defaultValue = "1") Integer page
    ) {
        StudyRoomSummariesDto studyRoomSummariesDto = getStudyRoomsService.studyRooms(page, userId);

        return ResponseEntity.ok(studyRoomSummariesDto);
    }

    @Operation(summary = "스터디룸 조회")
    @GetMapping("api/studyrooms/{studyRoomId}")
    public ResponseEntity<StudyRoomDetailDto> studyRoom(
            @RequestAttribute UserId userId,
            @PathVariable Long studyRoomId
    ) {
        StudyRoomDetailDto studyRoomDetailDto = getStudyRoomService.studyRoom(studyRoomId, userId);

        return ResponseEntity.ok(studyRoomDetailDto);
    }

    @Operation(summary = "스터디룸 생성")
    @ApiResponse(responseCode = "201")
    @PostMapping("api/studyrooms")
    public ResponseEntity<CreateStudyRoomResponseDto> create(
            @RequestAttribute UserId userId,
            @Validated @RequestBody CreateStudyRoomRequestDto requestDto
    ) {
        CreateStudyRoomRequest request = CreateStudyRoomRequest.of(requestDto);

        Long studyRoomId = createStudyRoomService.create(request, userId);

        Long participateId = participateService.participate(userId, StudyRoomId.of(studyRoomId));

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new CreateStudyRoomResponseDto(studyRoomId, participateId));
    }

    @Operation(summary = "다음 스터디 단계 진행")
    @MessageMapping("/studyrooms/{studyRoomId}/next-step")
    public void proceedToNextStep(
            @DestinationVariable Long studyRoomId,
            @Payload StudyProgressRequestDto requestDto,
            SimpMessageHeaderAccessor headerAccessor
    ) {
        UserId userId = (UserId) headerAccessor.getSessionAttributes().get("UserId");

        studyProgressService.proceedToNextStep(
                userId, StudyRoomId.of(studyRoomId), Step.valueOf(requestDto.step()));
    }
}
