package com.pomodoro.pomodoromate.studyRoom.applications;

import com.pomodoro.pomodoromate.participant.applications.GetParticipantsService;
import com.pomodoro.pomodoromate.participant.dtos.ParticipantSummariesDto;
import com.pomodoro.pomodoromate.participant.exceptions.ParticipantNotFoundException;
import com.pomodoro.pomodoromate.participant.models.Participant;
import com.pomodoro.pomodoromate.participant.models.ParticipantId;
import com.pomodoro.pomodoromate.participant.repositories.ParticipantRepository;
import com.pomodoro.pomodoromate.studyRoom.exceptions.StudyRoomNotFoundException;
import com.pomodoro.pomodoromate.studyRoom.models.StudyRoom;
import com.pomodoro.pomodoromate.studyRoom.models.StudyRoomId;
import com.pomodoro.pomodoromate.studyRoom.repositories.StudyRoomRepository;
import com.pomodoro.pomodoromate.user.models.UserId;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class StudyRoomHostService {
    private final StudyRoomRepository studyRoomRepository;
    private final ParticipantRepository participantRepository;
    private final GetParticipantsService getParticipantsService;
    private final SimpMessagingTemplate messagingTemplate;

    public StudyRoomHostService(StudyRoomRepository studyRoomRepository,
                                ParticipantRepository participantRepository,
                                GetParticipantsService getParticipantsService,
                                SimpMessagingTemplate messagingTemplate) {
        this.studyRoomRepository = studyRoomRepository;
        this.participantRepository = participantRepository;
        this.getParticipantsService = getParticipantsService;
        this.messagingTemplate = messagingTemplate;
    }

    @Transactional
    public void transferHost(UserId userId, StudyRoomId studyRoomId, ParticipantId newHostId) {
        StudyRoom studyRoom = studyRoomRepository.findById(studyRoomId.value())
                .orElseThrow(StudyRoomNotFoundException::new);

        Participant hostParticipant = participantRepository.findBy(userId, studyRoomId)
                .orElseThrow(ParticipantNotFoundException::new);

        studyRoom.validateHost(hostParticipant.id());

        Participant newHostParticipant = participantRepository.findBy(newHostId)
                .orElseThrow(ParticipantNotFoundException::new);

        studyRoom.excludeHost();

        assignHost(newHostParticipant.id(), studyRoomId);

        messagingTemplate.convertAndSend("/sub/studyrooms/" + studyRoomId + "/host", newHostId);
    }

    @Transactional
    public void autoTransferHost(StudyRoomId studyRoomId, ParticipantId hostId) {
        StudyRoom studyRoom = studyRoomRepository.findById(studyRoomId.value())
                .orElseThrow(StudyRoomNotFoundException::new);

        Participant mostRecentParticipant = participantRepository.findMostRecentBy(studyRoomId)
                .orElseThrow(ParticipantNotFoundException::new);

        studyRoom.validateHost(hostId);

        studyRoom.excludeHost();

        assignHost(mostRecentParticipant.id(), studyRoomId);
    }

    @Transactional
    public void assignHost(ParticipantId participantId, StudyRoomId studyRoomId) {
        StudyRoom studyRoom = studyRoomRepository.findById(studyRoomId.value())
                .orElseThrow(StudyRoomNotFoundException::new);

        studyRoom.validateIncomplete();

        Participant participant = participantRepository.findById(participantId.value())
                .orElseThrow(ParticipantNotFoundException::new);

        studyRoom.checkHostExists();

        studyRoom.assignHost(participant.id());
    }
}
