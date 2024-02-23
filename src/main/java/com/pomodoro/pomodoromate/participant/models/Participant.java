package com.pomodoro.pomodoromate.participant.models;

import com.pomodoro.pomodoromate.auth.exceptions.UnauthorizedException;
import com.pomodoro.pomodoromate.common.exceptions.AuthorizationException;
import com.pomodoro.pomodoromate.common.models.BaseEntity;
import com.pomodoro.pomodoromate.common.models.Status;
import com.pomodoro.pomodoromate.studyRoom.exceptions.StudyRoomMismatchException;
import com.pomodoro.pomodoromate.studyRoom.models.StudyRoomId;
import com.pomodoro.pomodoromate.user.models.UserId;
import jakarta.persistence.AttributeOverride;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Builder;

@Entity
public class Participant extends BaseEntity {
    @Id
    @GeneratedValue
    private Long id;

    @Embedded
    @AttributeOverride(name = "value", column = @Column(name = "studyRoomId"))
    private StudyRoomId studyRoomId;

    @Embedded
    @AttributeOverride(name = "value", column = @Column(name = "userId"))
    private UserId userId;

    @Enumerated(EnumType.STRING)
    private Status status;

    public Participant() {
    }

    @Builder
    public Participant(Long id, StudyRoomId studyRoomId, UserId userId) {
        this.id = id;
        this.studyRoomId = studyRoomId;
        this.userId = userId;
        this.status = Status.ACTIVE;
    }

    public ParticipantId id() {
        return ParticipantId.of(id);
    }

    public StudyRoomId studyRoomId() {
        return studyRoomId;
    }

    public UserId userId() {
        return userId;
    }

    public Status status() {
        return status;
    }

    public void validateParticipant(UserId userId) {
        if (!this.userId.equals(userId)) {
            throw new AuthorizationException();
        }
    }

    public void delete() {
        this.status = Status.DELETED;
    }

    public void validateStudyRoom(StudyRoomId studyRoomId) {
        if (!this.studyRoomId.equals(studyRoomId)) {
            throw new StudyRoomMismatchException();
        }
    }
}
