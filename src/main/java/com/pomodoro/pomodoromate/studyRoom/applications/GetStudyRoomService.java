package com.pomodoro.pomodoromate.studyRoom.applications;

import com.pomodoro.pomodoromate.chat.models.Chat;
import com.pomodoro.pomodoromate.chat.repositories.ChatRepository;
import com.pomodoro.pomodoromate.participant.dtos.ParticipantSummaryDto;
import com.pomodoro.pomodoromate.participant.models.Participant;
import com.pomodoro.pomodoromate.participant.repositories.ParticipantRepository;
import com.pomodoro.pomodoromate.studyRoom.dtos.StudyRoomDetailDto;
import com.pomodoro.pomodoromate.studyRoom.exceptions.StudyRoomNotFoundException;
import com.pomodoro.pomodoromate.studyRoom.models.StudyRoom;
import com.pomodoro.pomodoromate.studyRoom.models.StudyRoomId;
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
    private final ChatRepository chatRepository;

    public GetStudyRoomService(StudyRoomRepository studyRoomRepository,
                               ParticipantRepository participantRepository,
                               ValidateUserService validateUserService,
                               ChatRepository chatRepository) {
        this.studyRoomRepository = studyRoomRepository;
        this.participantRepository = participantRepository;
        this.validateUserService = validateUserService;
        this.chatRepository = chatRepository;
    }

    @Transactional(readOnly = true)
    public StudyRoomDetailDto studyRoom(Long studyRoomId, UserId userId) {
        validateUserService.validate(userId);

        StudyRoom studyRoom = studyRoomRepository.findById(studyRoomId)
                .orElseThrow(StudyRoomNotFoundException::new);

        List<Participant> participants = participantRepository.findAllNotDeletedBy(studyRoom.id());

        List<Chat> chats = chatRepository.findAllBy(StudyRoomId.of(studyRoomId));

        return studyRoom.toDetailDto(participants, chats);
    }
}
