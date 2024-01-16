package com.pomodoro.pomodoromate.studyRoom.controllers;

import com.pomodoro.pomodoromate.studyRoom.applications.CreateStudyRoomService;
import com.pomodoro.pomodoromate.studyRoom.dtos.CreateStudyRoomRequest;
import com.pomodoro.pomodoromate.studyRoom.dtos.CreateStudyRoomRequestDto;
import com.pomodoro.pomodoromate.user.models.UserId;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;

@Tag(name = "스터디룸 API")
@RestController
@RequestMapping("studyrooms")
public class StudyRoomController {
    private final CreateStudyRoomService createStudyRoomService;

    public StudyRoomController(CreateStudyRoomService createStudyRoomService) {
        this.createStudyRoomService = createStudyRoomService;
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
}
