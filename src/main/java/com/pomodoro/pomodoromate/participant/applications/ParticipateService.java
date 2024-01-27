package com.pomodoro.pomodoromate.participant.applications;

import com.pomodoro.pomodoromate.participant.models.Participant;
import com.pomodoro.pomodoromate.participant.repositories.ParticipantRepository;
import com.pomodoro.pomodoromate.auth.exceptions.UnauthorizedException;
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
public class ParticipateService {
    private final ParticipantRepository participantRepository;
    private final UserRepository userRepository;
    private final StudyRoomRepository studyRoomRepository;

    public ParticipateService(ParticipantRepository participantRepository,
                              UserRepository userRepository,
                              StudyRoomRepository studyRoomRepository) {
        this.participantRepository = participantRepository;
        this.userRepository = userRepository;
        this.studyRoomRepository = studyRoomRepository;
    }

    @Transactional
    public Long participate(UserId userId, StudyRoomId studyRoomId) {
        User user = userRepository.findById(userId.getValue())
                .orElseThrow(UnauthorizedException::new);

        StudyRoom studyRoom = studyRoomRepository.findById(studyRoomId.getValue())
                .orElseThrow(StudyRoomNotFoundException::new);

        Participant participant = Participant.builder()
                .studyRoomId(studyRoom.id())
                .userId(user.id())
                .build();

        Participant saved = participantRepository.save(participant);

        return saved.id().getValue();
    }
}
