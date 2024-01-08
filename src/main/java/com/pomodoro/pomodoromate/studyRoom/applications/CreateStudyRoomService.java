package com.pomodoro.pomodoromate.studyRoom.applications;

import com.pomodoro.pomodoromate.studyRoom.dtos.CreateStudyRoomRequest;
import com.pomodoro.pomodoromate.studyRoom.models.StudyRoom;
import com.pomodoro.pomodoromate.studyRoom.models.StudyRoomInfo;
import com.pomodoro.pomodoromate.studyRoom.repositories.StudyRoomRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CreateStudyRoomService {
    private final StudyRoomRepository studyRoomRepository;
    private final PasswordEncoder passwordEncoder;

    public CreateStudyRoomService(StudyRoomRepository studyRoomRepository,
                                  PasswordEncoder passwordEncoder) {
        this.studyRoomRepository = studyRoomRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public Long create(CreateStudyRoomRequest request) {
        StudyRoom studyRoom = StudyRoom.builder()
                .info(request.getInfo())
                .build();

//        if (request.getStatus().equals(StudyRoomStatus.PASSWORD_PROTECTED)) {
//            studyRoom.changePassword(request.getPassword(), passwordEncoder);
//        }

        StudyRoom saved = studyRoomRepository.save(studyRoom);

        return saved.id();
    }
}
