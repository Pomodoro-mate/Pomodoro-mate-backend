package com.pomodoro.pomodoromate.participant.applications;

import com.pomodoro.pomodoromate.auth.exceptions.UnauthorizedException;
import com.pomodoro.pomodoromate.participant.exceptions.ParticipantNotFoundException;
import com.pomodoro.pomodoromate.participant.models.Participant;
import com.pomodoro.pomodoromate.participant.models.ParticipantId;
import com.pomodoro.pomodoromate.participant.repositories.ParticipantRepository;
import com.pomodoro.pomodoromate.studyRoom.applications.CompleteStudyRoomService;
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

    public LeaveStudyService(ParticipantRepository participantRepository,
                             UserRepository userRepository,
                             StudyRoomRepository studyRoomRepository,
                             CompleteStudyRoomService completeStudyRoomService) {
        this.participantRepository = participantRepository;
        this.userRepository = userRepository;
        this.studyRoomRepository = studyRoomRepository;
        this.completeStudyRoomService = completeStudyRoomService;
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

        Long participantCount = participantRepository.countActiveByStudyRoomId(studyRoomId);

        if (participantCount == 0) {
            completeStudyRoomService.completeStudy(studyRoomId);
        }
    }
}
