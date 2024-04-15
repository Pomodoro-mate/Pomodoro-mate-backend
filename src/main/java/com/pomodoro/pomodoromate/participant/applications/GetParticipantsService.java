package com.pomodoro.pomodoromate.participant.applications;

import com.pomodoro.pomodoromate.participant.dtos.ParticipantSummariesDto;
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
        List<Participant> participants = participantRepository.findAllActiveBy(studyRoomId);

        return new ParticipantSummariesDto(participants.stream()
                .map(Participant::toSummaryDto).toList());
    }
}
