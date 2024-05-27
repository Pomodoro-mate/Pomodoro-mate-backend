package com.pomodoro.pomodoromate.participant.applications;

import com.pomodoro.pomodoromate.auth.exceptions.UnauthorizedException;
import com.pomodoro.pomodoromate.participant.exceptions.ParticipantNotFoundException;
import com.pomodoro.pomodoromate.participant.models.Participant;
import com.pomodoro.pomodoromate.participant.models.ParticipantId;
import com.pomodoro.pomodoromate.participant.repositories.ParticipantRepository;
import com.pomodoro.pomodoromate.studyRoom.applications.CompleteStudyRoomService;
import com.pomodoro.pomodoromate.studyRoom.applications.StudyRoomHostService;
import com.pomodoro.pomodoromate.studyRoom.exceptions.StudyRoomNotFoundException;
import com.pomodoro.pomodoromate.studyRoom.models.StudyRoom;
import com.pomodoro.pomodoromate.studyRoom.models.StudyRoomId;
import com.pomodoro.pomodoromate.studyRoom.repositories.StudyRoomRepository;
import com.pomodoro.pomodoromate.user.models.User;
import com.pomodoro.pomodoromate.user.models.UserId;
import com.pomodoro.pomodoromate.user.repositories.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class LeaveStudyService {
    private final ParticipantRepository participantRepository;
    private final UserRepository userRepository;
    private final StudyRoomRepository studyRoomRepository;
    private final CompleteStudyRoomService completeStudyRoomService;
    private final StudyRoomHostService studyRoomHostService;

    public LeaveStudyService(ParticipantRepository participantRepository,
                             UserRepository userRepository,
                             StudyRoomRepository studyRoomRepository,
                             CompleteStudyRoomService completeStudyRoomService,
                             StudyRoomHostService studyRoomHostService) {
        this.participantRepository = participantRepository;
        this.userRepository = userRepository;
        this.studyRoomRepository = studyRoomRepository;
        this.completeStudyRoomService = completeStudyRoomService;
        this.studyRoomHostService = studyRoomHostService;
    }

    @Transactional
    public void leaveStudy(UserId userId, StudyRoomId studyRoomId, ParticipantId participantId) {
        User user = userRepository.findById(userId.value())
                .orElseThrow(UnauthorizedException::new);

        StudyRoom studyRoom = studyRoomRepository.findById(studyRoomId.value())
                .orElseThrow(StudyRoomNotFoundException::new);

        Participant participant = participantRepository.findById(participantId.value())
                .orElseThrow(ParticipantNotFoundException::new);

        participant.validateStudyRoom(studyRoom.id());
        participant.validateParticipant(user.id());

        participant.delete();

        if (participant.isHost(studyRoom.hostId().value())) {
            studyRoomHostService.transferHost(studyRoomId, participantId);
        }

        Long participantCount = participantRepository.countNotDeletedBy(studyRoomId);

        if (participantCount == 0) {
            completeStudyRoomService.completeStudy(studyRoomId);
        }
    }

    @Transactional
    public void leaveStudy(UserId userId, StudyRoomId studyRoomId) {
        Participant participant = participantRepository.findBy(userId, studyRoomId)
                .orElseThrow(ParticipantNotFoundException::new);

        participant.delete();

        Long participantCount = participantRepository.countNotDeletedBy(studyRoomId);

        if (participantCount == 0) {
            completeStudyRoomService.completeStudy(studyRoomId);
        }
    }
}
