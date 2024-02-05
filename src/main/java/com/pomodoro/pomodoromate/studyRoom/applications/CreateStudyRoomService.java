package com.pomodoro.pomodoromate.studyRoom.applications;

import com.pomodoro.pomodoromate.studyRoom.dtos.CreateStudyRoomRequest;
import com.pomodoro.pomodoromate.studyRoom.models.StudyRoom;
import com.pomodoro.pomodoromate.studyRoom.repositories.StudyRoomRepository;
import com.pomodoro.pomodoromate.user.applications.ValidateUserService;
import com.pomodoro.pomodoromate.user.models.UserId;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CreateStudyRoomService {
    private final StudyRoomRepository studyRoomRepository;
    private final ValidateUserService validateUserService;
    private final PasswordEncoder passwordEncoder;

    public CreateStudyRoomService(StudyRoomRepository studyRoomRepository,
                                  ValidateUserService validateUserService,
                                  PasswordEncoder passwordEncoder) {
        this.studyRoomRepository = studyRoomRepository;
        this.validateUserService = validateUserService;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public Long create(CreateStudyRoomRequest request, UserId userId) {
        validateUserService.validate(userId);

        StudyRoom studyRoom = StudyRoom.builder()
                .info(request.getInfo())
                .build();

//        if (request.getStatus().equals(StudyRoomStatus.PASSWORD_PROTECTED)) {
//            studyRoom.changePassword(request.getPassword(), passwordEncoder);
//        }

        StudyRoom saved = studyRoomRepository.save(studyRoom);

        return saved.id().getValue();
    }
}
