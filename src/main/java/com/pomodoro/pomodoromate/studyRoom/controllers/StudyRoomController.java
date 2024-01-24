package com.pomodoro.pomodoromate.studyRoom.controllers;

import com.pomodoro.pomodoromate.common.dtos.PageDto;
import com.pomodoro.pomodoromate.studyRoom.applications.CreateStudyRoomService;
import com.pomodoro.pomodoromate.studyRoom.applications.GetStudyRoomsService;
import com.pomodoro.pomodoromate.studyRoom.dtos.CreateStudyRoomRequest;
import com.pomodoro.pomodoromate.studyRoom.dtos.CreateStudyRoomRequestDto;
import com.pomodoro.pomodoromate.studyRoom.dtos.StudyRoomSummaryDto;
import com.pomodoro.pomodoromate.studyRoom.dtos.StudyRoomSummariesDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.util.List;

@Tag(name = "스터디룸 API")
@RestController
@RequestMapping("studyrooms")
public class StudyRoomController {
    private final CreateStudyRoomService createStudyRoomService;
    private final GetStudyRoomsService getStudyRoomsService;

    public StudyRoomController(CreateStudyRoomService createStudyRoomService,
                               GetStudyRoomsService getStudyRoomsService) {
        this.createStudyRoomService = createStudyRoomService;
        this.getStudyRoomsService = getStudyRoomsService;
    }

    @Operation(summary = "스터디룸 생성")
    @PostMapping
    public ResponseEntity<Void> create(
            @Validated @RequestBody CreateStudyRoomRequestDto requestDto
    ) {
        CreateStudyRoomRequest request = CreateStudyRoomRequest.of(requestDto);

        Long studyRoomId = createStudyRoomService.create(request);

        return ResponseEntity.created(URI.create("/studyrooms/" + studyRoomId)).build();
    }

    @Operation(summary = "스터디룸 목록 조회")
    @GetMapping
    public ResponseEntity<StudyRoomSummariesDto> studyrooms(
            @RequestParam(required = false, defaultValue = "1") Integer page
    ) {
        StudyRoomSummariesDto studyRoomSummariesDto = getStudyRoomsService.studyRooms(page);

        return ResponseEntity.ok(studyRoomSummariesDto);
    }
}
