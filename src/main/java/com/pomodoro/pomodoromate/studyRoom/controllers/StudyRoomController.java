package com.pomodoro.pomodoromate.studyRoom.controllers;

import com.pomodoro.pomodoromate.studyRoom.applications.CreateStudyRoomService;
import com.pomodoro.pomodoromate.studyRoom.dtos.CreateStudyRoomRequest;
import com.pomodoro.pomodoromate.studyRoom.dtos.CreateStudyRoomRequestDto;
import com.pomodoro.pomodoromate.studyRoom.exceptions.StudyRoomIntroLengthException;
import com.pomodoro.pomodoromate.studyRoom.exceptions.StudyRoomNameLengthException;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;

@RestController
@RequestMapping("studyrooms")
public class StudyRoomController {
    private final CreateStudyRoomService createStudyRoomService;

    public StudyRoomController(CreateStudyRoomService createStudyRoomService) {
        this.createStudyRoomService = createStudyRoomService;
    }

    @PostMapping
    public ResponseEntity<Void> create(
            @Validated @RequestBody CreateStudyRoomRequestDto requestDto
    ) {
        CreateStudyRoomRequest request = CreateStudyRoomRequest.of(requestDto);

        Long studyRoomId = createStudyRoomService.create(request);

        return ResponseEntity.created(URI.create("/studyrooms/" + studyRoomId)).build();
    }
}
