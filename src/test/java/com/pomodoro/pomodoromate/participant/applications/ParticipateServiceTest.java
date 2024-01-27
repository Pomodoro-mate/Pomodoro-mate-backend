package com.pomodoro.pomodoromate.participant.applications;

import com.pomodoro.pomodoromate.auth.exceptions.UnauthorizedException;
import com.pomodoro.pomodoromate.participant.models.Participant;
import com.pomodoro.pomodoromate.participant.repositories.ParticipantRepository;
import com.pomodoro.pomodoromate.studyRoom.exceptions.StudyRoomNotFoundException;
import com.pomodoro.pomodoromate.studyRoom.models.StudyRoom;
import com.pomodoro.pomodoromate.studyRoom.models.StudyRoomId;
import com.pomodoro.pomodoromate.studyRoom.repositories.StudyRoomRepository;
import com.pomodoro.pomodoromate.user.models.User;
import com.pomodoro.pomodoromate.user.models.UserId;
import com.pomodoro.pomodoromate.user.repositories.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

class ParticipateServiceTest {
    private ParticipantRepository participantRepository;
    private UserRepository userRepository;
    private StudyRoomRepository studyRoomRepository;
    private ParticipateService participateService;

    @BeforeEach
    void setUp() {
        participantRepository = mock(ParticipantRepository.class);
        userRepository = mock(UserRepository.class);
        studyRoomRepository = mock(StudyRoomRepository.class);
        participateService = new ParticipateService(
                participantRepository,
                userRepository,
                studyRoomRepository
        );
    }

    @Test
    void participate() {
        User user = User.builder()
                .id(1L)
                .build();

        StudyRoom studyRoom = StudyRoom.builder()
                .id(1L)
                .build();

        Participant participant = Participant.builder()
                .id(1L)
                .studyRoomId(studyRoom.id())
                .userId(user.id())
                .build();

        given(userRepository.findById(user.id().getValue()))
                .willReturn(Optional.of(user));

        given(studyRoomRepository.findById(studyRoom.id().getValue()))
                .willReturn(Optional.of(studyRoom));

        given(participantRepository.save(any()))
                .willReturn(participant);

        Long savedId = participateService.participate(user.id(), studyRoom.id());

        assertThat(savedId).isEqualTo(1L);
    }

    @Test
    void participateWithUnauthorized() {
        UserId invalidUserId = UserId.of(999_999L);

        StudyRoomId studyRoomId = StudyRoomId.of(1L);

        given(userRepository.findById(invalidUserId.getValue()))
                .willThrow(UnauthorizedException.class);

        assertThrows(UnauthorizedException.class,
                () -> participateService.participate(invalidUserId, studyRoomId));
    }

    @Test
    void participateWithStudyRoomNotFoundException() {
        User user = User.builder()
                .id(1L)
                .build();

        StudyRoomId invalidStudyRoomId = StudyRoomId.of(999_999L);

        given(userRepository.findById(user.id().getValue()))
                .willReturn(Optional.of(user));

        given(studyRoomRepository.findById(invalidStudyRoomId.getValue()))
                .willThrow(StudyRoomNotFoundException.class);

        assertThrows(StudyRoomNotFoundException.class,
                () -> participateService.participate(user.id(), invalidStudyRoomId));
    }
}
