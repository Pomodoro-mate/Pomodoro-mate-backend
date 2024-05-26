package com.pomodoro.pomodoromate.studyRoom.applications;

import com.pomodoro.pomodoromate.participant.exceptions.ParticipantNotFoundException;
import com.pomodoro.pomodoromate.participant.models.Participant;
import com.pomodoro.pomodoromate.participant.repositories.ParticipantRepository;
import com.pomodoro.pomodoromate.studyRoom.dtos.EditStudyRoomRequest;
import com.pomodoro.pomodoromate.studyRoom.exceptions.StudyRoomNotFoundException;
import com.pomodoro.pomodoromate.studyRoom.models.StudyRoom;
import com.pomodoro.pomodoromate.studyRoom.models.StudyRoomId;
import com.pomodoro.pomodoromate.studyRoom.repositories.StudyRoomRepository;
import com.pomodoro.pomodoromate.user.applications.ValidateUserService;
import com.pomodoro.pomodoromate.user.models.UserId;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class EditStudyRoomService {
    private final StudyRoomRepository studyRoomRepository;
    private final ParticipantRepository participantRepository;
    private final ValidateUserService validateUserService;

    public EditStudyRoomService(StudyRoomRepository studyRoomRepository,
                                ParticipantRepository participantRepository,
                                ValidateUserService validateUserService) {
        this.studyRoomRepository = studyRoomRepository;
        this.participantRepository = participantRepository;
        this.validateUserService = validateUserService;
    }

    @Transactional
    public void edit(EditStudyRoomRequest request, StudyRoomId studyRoomId, UserId userId) {
        validateUserService.validate(userId);

        StudyRoom studyRoom = studyRoomRepository.findByIdForUpdate(studyRoomId.value())
                .orElseThrow(StudyRoomNotFoundException::new);

        Participant participant = participantRepository.findById(request.getParticipantId().value())
                .orElseThrow(ParticipantNotFoundException::new);

        participant.validateStudyRoom(studyRoomId);
        participant.validateParticipant(userId);
        participant.validateActive();

        studyRoom.validateHost(participant.id());

        studyRoom.update(
                request.getInfo(),
                request.getTimeSet());
    }
}
