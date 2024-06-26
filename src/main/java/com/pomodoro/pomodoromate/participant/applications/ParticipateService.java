package com.pomodoro.pomodoromate.participant.applications;

import com.pomodoro.pomodoromate.auth.exceptions.UnauthorizedException;
import com.pomodoro.pomodoromate.participant.dtos.ParticipateRequest;
import com.pomodoro.pomodoromate.participant.models.Participant;
import com.pomodoro.pomodoromate.participant.repositories.ParticipantRepository;
import com.pomodoro.pomodoromate.studyRoom.applications.StudyRoomHostService;
import com.pomodoro.pomodoromate.studyRoom.exceptions.ParticipatingRoomExistsException;
import com.pomodoro.pomodoromate.studyRoom.exceptions.StudyRoomNotFoundException;
import com.pomodoro.pomodoromate.studyRoom.models.StudyRoom;
import com.pomodoro.pomodoromate.studyRoom.models.StudyRoomId;
import com.pomodoro.pomodoromate.studyRoom.repositories.StudyRoomRepository;
import com.pomodoro.pomodoromate.user.models.User;
import com.pomodoro.pomodoromate.user.models.UserId;
import com.pomodoro.pomodoromate.user.repositories.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class ParticipateService {
    private final ParticipantRepository participantRepository;
    private final UserRepository userRepository;
    private final StudyRoomRepository studyRoomRepository;
    private final LeaveStudyService leaveStudyService;
    private final StudyRoomHostService studyRoomHostService;

    public ParticipateService(ParticipantRepository participantRepository,
                              UserRepository userRepository,
                              StudyRoomRepository studyRoomRepository,
                              LeaveStudyService leaveStudyService,
                              StudyRoomHostService studyRoomHostService) {
        this.participantRepository = participantRepository;
        this.userRepository = userRepository;
        this.studyRoomRepository = studyRoomRepository;
        this.leaveStudyService = leaveStudyService;
        this.studyRoomHostService = studyRoomHostService;
    }

    @Transactional
    public Long participate(ParticipateRequest request, UserId userId, StudyRoomId studyRoomId) {
        User user = userRepository.findById(userId.value())
                .orElseThrow(UnauthorizedException::new);

        checkParticipatingRoom(userId, request.isForce());

        StudyRoom studyRoom = studyRoomRepository.findByIdForUpdate(studyRoomId.value())
                .orElseThrow(StudyRoomNotFoundException::new);

        studyRoom.validateIncomplete();

        Long participantCount = participantRepository.countNotDeletedBy(studyRoomId);

        studyRoom.validateMaxParticipantExceeded(participantCount);

        return createOrUpdateParticipant(userId, studyRoomId, user, studyRoom).id().value();
    }

    @Transactional
    public Long participateForCreator(UserId userId, StudyRoomId studyRoomId) {
        User user = userRepository.findById(userId.value())
                .orElseThrow(UnauthorizedException::new);

        StudyRoom studyRoom = studyRoomRepository.findByIdForUpdate(studyRoomId.value())
                .orElseThrow(StudyRoomNotFoundException::new);

        studyRoom.validateIncomplete();

        Long participantCount = participantRepository.countNotDeletedBy(studyRoomId);

        studyRoom.validateMaxParticipantExceeded(participantCount);

        Participant createdParticipant = createOrUpdateParticipant(userId, studyRoomId, user, studyRoom);

        studyRoomHostService.assignHost(createdParticipant.id(), studyRoomId);

        return createdParticipant.id().value();
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

    private Participant createOrUpdateParticipant(UserId userId, StudyRoomId studyRoomId, User user, StudyRoom studyRoom) {
        Optional<Participant> existingParticipant = participantRepository.findBy(userId, studyRoomId);

        if (existingParticipant.isPresent()) {
            existingParticipant.get().activate();

            return existingParticipant.get();
        }

        Participant participant = Participant.builder()
                .studyRoomId(studyRoom.id())
                .userId(user.id())
                .userInfo(user.info())
                .build();

        Participant saved = participantRepository.save(participant);

        return saved;
    }
}
