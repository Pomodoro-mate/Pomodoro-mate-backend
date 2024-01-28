package com.pomodoro.pomodoromate.studyRoom.applications;

import com.pomodoro.pomodoromate.participant.repositories.ParticipantRepository;
import com.pomodoro.pomodoromate.studyRoom.dtos.StudyRoomDetailDto;
import com.pomodoro.pomodoromate.studyRoom.exceptions.StudyRoomNotFoundException;
import com.pomodoro.pomodoromate.studyRoom.models.StudyRoom;
import com.pomodoro.pomodoromate.studyRoom.repositories.StudyRoomRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class GetStudyRoomService {
    private final StudyRoomRepository studyRoomRepository;
    private final ParticipantRepository participantRepository;

    public GetStudyRoomService(StudyRoomRepository studyRoomRepository,
                               ParticipantRepository participantRepository) {
        this.studyRoomRepository = studyRoomRepository;
        this.participantRepository = participantRepository;
    }

    @Transactional(readOnly = true)
    public StudyRoomDetailDto studyRoom(Long studyRoomId) {
        StudyRoom studyRoom = studyRoomRepository.findById(studyRoomId)
                .orElseThrow(StudyRoomNotFoundException::new);

        Long participantCount = participantRepository.countActiveByStudyRoomId(studyRoom.id());

        return studyRoom.toDetailDto(participantCount);
    }
}
