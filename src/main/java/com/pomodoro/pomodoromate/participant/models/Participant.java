package com.pomodoro.pomodoromate.participant.models;

import com.pomodoro.pomodoromate.common.exceptions.AuthorizationException;
import com.pomodoro.pomodoromate.common.models.BaseEntity;
import com.pomodoro.pomodoromate.common.models.SessionId;
import com.pomodoro.pomodoromate.common.models.Status;
import com.pomodoro.pomodoromate.participant.dtos.ParticipantSummaryDto;
import com.pomodoro.pomodoromate.studyRoom.exceptions.StudyRoomMismatchException;
import com.pomodoro.pomodoromate.studyRoom.models.StudyRoomId;
import com.pomodoro.pomodoromate.user.models.UserId;
import com.pomodoro.pomodoromate.user.models.UserInfo;
import jakarta.persistence.AttributeOverride;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
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

    @Embedded
    private UserInfo userInfo;

    @Embedded
    private SessionId sessionId;

    @Enumerated(EnumType.STRING)
    private Status status;

    @Builder
    public Participant(Long id, StudyRoomId studyRoomId, UserId userId, UserInfo userInfo) {
        this.id = id;
        this.studyRoomId = studyRoomId;
        this.userId = userId;
        this.userInfo = userInfo;
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

    public UserInfo userInfo() {
        return userInfo;
    }

    public Status status() {
        return status;
    }

    public SessionId sessionId() {
        return sessionId;
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

    public ParticipantSummaryDto toSummaryDto() {
        return new ParticipantSummaryDto(id, userId.value(),
                userInfo.nickname(), userInfo.imageUrl());
    }

    public void activate(SessionId sessionId) {
        this.sessionId = sessionId;
        this.status = Status.ACTIVE;
    }

    public void activate() {
        this.status = Status.ACTIVE;
    }
}
