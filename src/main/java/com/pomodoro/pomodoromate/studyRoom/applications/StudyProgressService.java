package com.pomodoro.pomodoromate.studyRoom.applications;

import com.pomodoro.pomodoromate.studyRoom.exceptions.StudyRoomNotFoundException;
import com.pomodoro.pomodoromate.studyRoom.models.Step;
import com.pomodoro.pomodoromate.studyRoom.models.StudyRoom;
import com.pomodoro.pomodoromate.studyRoom.models.StudyRoomId;
import com.pomodoro.pomodoromate.studyRoom.repositories.StudyRoomRepository;
import com.pomodoro.pomodoromate.user.applications.ValidateUserService;
import com.pomodoro.pomodoromate.user.models.UserId;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class StudyProgressService {
    private final ValidateUserService validateUserService;
    private final StudyRoomRepository studyRoomRepository;
    private final SimpMessagingTemplate messagingTemplate;

    public StudyProgressService(ValidateUserService validateUserService,
                                StudyRoomRepository studyRoomRepository,
                                SimpMessagingTemplate messagingTemplate) {
        this.validateUserService = validateUserService;
        this.studyRoomRepository = studyRoomRepository;
        this.messagingTemplate = messagingTemplate;
    }

    @Transactional
    public void proceedToNextStep(UserId userId, StudyRoomId studyRoomId, Step step) {
        validateUserService.validate(userId);

        StudyRoom studyRoom = studyRoomRepository.findById(studyRoomId.value())
                .orElseThrow(StudyRoomNotFoundException::new);

        studyRoom.validateCurrentStep(step);
        studyRoom.validateIncomplete();

        studyRoom.proceedToNextStep();

        messagingTemplate.convertAndSend(
                "/sub/studyrooms/" + studyRoomId.value() + "/next-step",
                studyRoom.toNextStepDto()
        );
    }
}
