package com.pomodoro.pomodoromate.participant.applications;

import com.pomodoro.pomodoromate.participant.dtos.ParticipantSummariesDto;
import com.pomodoro.pomodoromate.participant.dtos.ParticipantSummaryDto;
import com.pomodoro.pomodoromate.participant.models.Participant;
import com.pomodoro.pomodoromate.participant.repositories.ParticipantRepository;
import com.pomodoro.pomodoromate.studyRoom.models.StudyRoomId;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class GetParticipantsService {
    private final ParticipantRepository participantRepository;

    public GetParticipantsService(ParticipantRepository participantRepository) {
        this.participantRepository = participantRepository;
    }

    @Transactional(readOnly = true)
    public ParticipantSummariesDto activeParticipants(StudyRoomId studyRoomId) {
        List<Participant> participants = participantRepository.findAllNotDeletedBy(studyRoomId);

        List<ParticipantSummaryDto> participantSummaryDtos = participants.stream()
                .map(participant -> participant.isHost(studyRoomId.value())
                        ? participant.toSummaryDto(true)
                        : participant.toSummaryDto(false))
                .toList();

        return new ParticipantSummariesDto(participantSummaryDtos);
    }
}
