package com.pomodoro.pomodoromate.chat.models;

import com.pomodoro.pomodoromate.chat.dtos.ChatSummaryDto;
import com.pomodoro.pomodoromate.common.models.BaseEntity;
import com.pomodoro.pomodoromate.participant.models.ParticipantId;
import com.pomodoro.pomodoromate.studyRoom.models.StudyRoomId;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Chat extends BaseEntity {
    @Id
    @GeneratedValue
    private Long id;

    @Embedded
    @AttributeOverride(name = "value", column = @Column(name = "studyRoomId"))
    private StudyRoomId studyRoomId;

    @Embedded
    @AttributeOverride(name = "value", column = @Column(name = "participantId"))
    private ParticipantId participantId;

    @Embedded
    private Message message;

    @Embedded
    private Writer writer;

    @Builder
    public Chat(Long id, StudyRoomId studyRoomId, ParticipantId participantId, Message message, Writer writer) {
        this.id = id;
        this.studyRoomId = studyRoomId;
        this.participantId = participantId;
        this.message = message;
        this.writer = writer;
    }

    public StudyRoomId studyRoomId() {
        return studyRoomId;
    }

    public ParticipantId participantId() {
        return participantId;
    }

    public ChatSummaryDto toSummaryDto() {
        return new ChatSummaryDto(id, studyRoomId.value(), message.value(), writer.value());
    }
}
