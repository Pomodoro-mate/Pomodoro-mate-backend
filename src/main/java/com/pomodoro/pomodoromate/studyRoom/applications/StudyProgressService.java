package com.pomodoro.pomodoromate.studyRoom.applications;

import com.pomodoro.pomodoromate.studyRoom.exceptions.StudyRoomNotFoundException;
import com.pomodoro.pomodoromate.studyRoom.models.Step;
import com.pomodoro.pomodoromate.studyRoom.models.StudyRoom;
import com.pomodoro.pomodoromate.studyRoom.models.StudyRoomId;
import com.pomodoro.pomodoromate.studyRoom.repositories.StudyRoomRepository;
import com.pomodoro.pomodoromate.user.applications.ValidateUserService;
import com.pomodoro.pomodoromate.user.models.UserId;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class StudyProgressService {
    private final ValidateUserService validateUserService;
    private final StudyRoomRepository studyRoomRepository;

    public StudyProgressService(ValidateUserService validateUserService,
                                StudyRoomRepository studyRoomRepository) {
        this.validateUserService = validateUserService;
        this.studyRoomRepository = studyRoomRepository;
    }

    @Transactional
    public void proceedToNextStep(UserId userId, StudyRoomId studyRoomId, Step step) {
        validateUserService.validate(userId);

        StudyRoom studyRoom = studyRoomRepository.findById(studyRoomId.value())
                .orElseThrow(StudyRoomNotFoundException::new);

        studyRoom.validateCurrentStep(step);
        studyRoom.validateIncomplete();

        studyRoom.proceedToNextStep();
    }
}
