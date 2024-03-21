package com.pomodoro.pomodoromate.participant.applications;

import com.pomodoro.pomodoromate.participant.dtos.ParticipantSummaryDto;
import com.pomodoro.pomodoromate.participant.models.Participant;
import com.pomodoro.pomodoromate.participant.repositories.ParticipantRepository;
import com.pomodoro.pomodoromate.studyRoom.applications.GetStudyRoomService;
import com.pomodoro.pomodoromate.studyRoom.models.StudyRoomId;
import com.pomodoro.pomodoromate.user.applications.ValidateUserService;
import com.pomodoro.pomodoromate.user.models.UserId;
import com.pomodoro.pomodoromate.user.models.UserInfo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

class GetParticipantsServiceTest {
    private ParticipantRepository participantRepository;
    private GetParticipantsService getParticipantsService;

    @BeforeEach
    void setUp() {
        participantRepository = mock(ParticipantRepository.class);
        getParticipantsService = new GetParticipantsService(participantRepository);
    }

    @Test
    void activeParticipants() {
        StudyRoomId studyRoomId = StudyRoomId.of(1L);
        UserId userId = UserId.of(1L);

        Participant participant = Participant.builder()
                .id(1L)
                .studyRoomId(studyRoomId)
                .userId(userId)
                .userInfo(new UserInfo())
                .build();

        given(participantRepository.findAllActiveBy(studyRoomId))
                .willReturn(List.of(participant));

        ParticipantSummariesDto participantSummariesDto = getParticipantsService
                .activeParticipants(studyRoomId);

        assertThat(participantSummariesDto.participantSummaries())
                .hasSize(1);
        assertThat(participantSummariesDto.participantSummaries().get(0).userId())
                .isEqualTo(userId.value());
    }
}
