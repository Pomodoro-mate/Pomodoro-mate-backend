package com.pomodoro.pomodoromate.studyRoom.applications;

import com.pomodoro.pomodoromate.participant.dtos.ParticipantSummaryDto;
import com.pomodoro.pomodoromate.participant.models.Participant;
import com.pomodoro.pomodoromate.participant.repositories.ParticipantRepository;
import com.pomodoro.pomodoromate.studyRoom.dtos.StudyRoomDetailDto;
import com.pomodoro.pomodoromate.studyRoom.exceptions.StudyRoomNotFoundException;
import com.pomodoro.pomodoromate.studyRoom.models.StudyRoom;
import com.pomodoro.pomodoromate.studyRoom.repositories.StudyRoomRepository;
import com.pomodoro.pomodoromate.user.applications.ValidateUserService;
import com.pomodoro.pomodoromate.user.models.UserId;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class GetStudyRoomService {
    private final StudyRoomRepository studyRoomRepository;
    private final ParticipantRepository participantRepository;
    private final ValidateUserService validateUserService;

    public GetStudyRoomService(StudyRoomRepository studyRoomRepository,
                               ParticipantRepository participantRepository,
                               ValidateUserService validateUserService) {
        this.studyRoomRepository = studyRoomRepository;
        this.participantRepository = participantRepository;
        this.validateUserService = validateUserService;
    }

    @Transactional(readOnly = true)
    public StudyRoomDetailDto studyRoom(Long studyRoomId, UserId userId) {
        validateUserService.validate(userId);

        StudyRoom studyRoom = studyRoomRepository.findById(studyRoomId)
                .orElseThrow(StudyRoomNotFoundException::new);

        List<Participant> participants = participantRepository.findAllActiveByStudyRoomId(studyRoom.id());

        List<ParticipantSummaryDto> participantSummaryDtos = participants.stream()
                .map(Participant::toSummaryDto).toList();

        return studyRoom.toDetailDto(participantSummaryDtos);
    }
}
