package com.pomodoro.pomodoromate.studyRoom.applications;

import com.pomodoro.pomodoromate.auth.exceptions.UnauthorizedException;
import com.pomodoro.pomodoromate.common.dtos.PageDto;
import com.pomodoro.pomodoromate.studyRoom.dtos.StudyRoomSummariesDto;
import com.pomodoro.pomodoromate.studyRoom.dtos.StudyRoomSummaryDto;
import com.pomodoro.pomodoromate.studyRoom.repositories.StudyRoomRepository;
import com.pomodoro.pomodoromate.user.applications.ValidateUserService;
import com.pomodoro.pomodoromate.user.models.UserId;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class GetStudyRoomsService {
    private final StudyRoomRepository studyRoomRepository;
    private final ValidateUserService validateUserService;

    public GetStudyRoomsService(StudyRoomRepository studyRoomRepository,
                                ValidateUserService validateUserService) {
        this.studyRoomRepository = studyRoomRepository;
        this.validateUserService = validateUserService;
    }

    @Transactional(readOnly = true)
    public StudyRoomSummariesDto studyRooms(Integer page, UserId userId) {
        validateUserService.validate(userId)
;
        Pageable pageable = PageRequest.of(page - 1, 8);

        Page<StudyRoomSummaryDto> studyRooms = findStudyRoomSummaries(pageable);

        PageDto pageDto = PageDto.of(page, studyRooms.getTotalPages());

        return new StudyRoomSummariesDto(
                studyRooms.toList(), pageDto
        );
    }

    private Page<StudyRoomSummaryDto> findStudyRoomSummaries(Pageable pageable) {
        return studyRoomRepository.findAllSummaryDto(pageable);
    }
}
