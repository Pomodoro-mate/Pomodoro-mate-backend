package com.pomodoro.pomodoromate.studyRoom.controllers;

import com.pomodoro.pomodoromate.studyRoom.applications.CreateStudyRoomService;
import com.pomodoro.pomodoromate.studyRoom.applications.GetStudyRoomService;
import com.pomodoro.pomodoromate.studyRoom.applications.GetStudyRoomsService;
import com.pomodoro.pomodoromate.studyRoom.dtos.CreateStudyRoomRequest;
import com.pomodoro.pomodoromate.studyRoom.dtos.CreateStudyRoomRequestDto;
import com.pomodoro.pomodoromate.studyRoom.dtos.StudyRoomDetailDto;
import com.pomodoro.pomodoromate.studyRoom.dtos.StudyRoomSummariesDto;
import com.pomodoro.pomodoromate.user.models.UserId;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;

@Tag(name = "스터디룸 API")
@RestController
@RequestMapping("studyrooms")
public class StudyRoomController {
    private final CreateStudyRoomService createStudyRoomService;
    private final GetStudyRoomsService getStudyRoomsService;
    private final GetStudyRoomService getStudyRoomService;

    public StudyRoomController(CreateStudyRoomService createStudyRoomService,
                               GetStudyRoomsService getStudyRoomsService,
                               GetStudyRoomService getStudyRoomService) {
        this.createStudyRoomService = createStudyRoomService;
        this.getStudyRoomsService = getStudyRoomsService;
        this.getStudyRoomService = getStudyRoomService;
    }

    @Operation(summary = "스터디룸 목록 조회")
    @GetMapping
    public ResponseEntity<StudyRoomSummariesDto> studyRooms(
            @RequestAttribute UserId userId,
            @RequestParam(required = false, defaultValue = "1") Integer page
    ) {
        StudyRoomSummariesDto studyRoomSummariesDto = getStudyRoomsService.studyRooms(page, userId);

        return ResponseEntity.ok(studyRoomSummariesDto);
    }

    @Operation(summary = "스터디룸 조회")
    @GetMapping("{studyRoomId}")
    public ResponseEntity<StudyRoomDetailDto> studyRoom(
            @RequestAttribute UserId userId,
            @PathVariable Long studyRoomId
    ) {
        StudyRoomDetailDto studyRoomDetailDto = getStudyRoomService.studyRoom(studyRoomId, userId);

        return ResponseEntity.ok(studyRoomDetailDto);
    }

    @Operation(summary = "스터디룸 생성")
    @ApiResponse(responseCode = "201")
    @PostMapping
    public ResponseEntity<Void> create(
            @RequestAttribute UserId userId,
            @Validated @RequestBody CreateStudyRoomRequestDto requestDto
    ) {
        CreateStudyRoomRequest request = CreateStudyRoomRequest.of(requestDto);

        Long studyRoomId = createStudyRoomService.create(request, userId);

        return ResponseEntity.created(URI.create("/studyrooms/" + studyRoomId)).build();
    }
}
