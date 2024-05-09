package com.pomodoro.pomodoromate.studyRoom.applications;

import com.pomodoro.pomodoromate.participant.applications.LeaveStudyService;
import com.pomodoro.pomodoromate.participant.models.ParticipantId;
import com.pomodoro.pomodoromate.studyRoom.dtos.CreateStudyRoomRequest;
import com.pomodoro.pomodoromate.studyRoom.exceptions.ParticipatingRoomExistsException;
import com.pomodoro.pomodoromate.studyRoom.models.MaxParticipantCount;
import com.pomodoro.pomodoromate.studyRoom.models.StudyRoom;
import com.pomodoro.pomodoromate.studyRoom.models.StudyRoomId;
import com.pomodoro.pomodoromate.studyRoom.repositories.StudyRoomRepository;
import com.pomodoro.pomodoromate.user.applications.ValidateUserService;
import com.pomodoro.pomodoromate.user.models.UserId;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class CreateStudyRoomService {
    private final StudyRoomRepository studyRoomRepository;
    private final ValidateUserService validateUserService;
    private final LeaveStudyService leaveStudyService;
    private final PasswordEncoder passwordEncoder;

    public CreateStudyRoomService(StudyRoomRepository studyRoomRepository,
                                  ValidateUserService validateUserService,
                                  LeaveStudyService leaveStudyService,
                                  PasswordEncoder passwordEncoder) {
        this.studyRoomRepository = studyRoomRepository;
        this.validateUserService = validateUserService;
        this.leaveStudyService = leaveStudyService;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public Long create(CreateStudyRoomRequest request, UserId userId) {
        validateUserService.validate(userId);

        checkParticipatingRoom(userId, request.isForce());

        StudyRoom studyRoom = createStudyRoomFromRequest(request);

//        if (request.getStatus().equals(StudyRoomStatus.PASSWORD_PROTECTED)) {
//            studyRoom.changePassword(request.getPassword(), passwordEncoder);
//        }

        StudyRoom saved = studyRoomRepository.save(studyRoom);

        return saved.id().value();
    }

    private void checkParticipatingRoom(UserId userId, boolean isForce) {
        Optional<StudyRoom> participatingRoom = studyRoomRepository.findParticipatingRoomBy(userId);

        if (participatingRoom.isPresent()) {
            if (isForce) {
                leaveStudyService.leaveStudy(userId, participatingRoom.get().id());
                return;
            }

            throw new ParticipatingRoomExistsException();
        }
    }

    private StudyRoom createStudyRoomFromRequest(CreateStudyRoomRequest request) {
        return StudyRoom.builder()
                .info(request.getInfo())
                .maxParticipantCount(MaxParticipantCount.of(8))
                .timeSet(request.getTimeSet())
                .build();
    }
}
