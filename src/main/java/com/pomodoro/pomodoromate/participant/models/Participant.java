package com.pomodoro.pomodoromate.participant.models;

import com.pomodoro.pomodoromate.common.exceptions.AuthorizationException;
import com.pomodoro.pomodoromate.common.models.BaseEntity;
import com.pomodoro.pomodoromate.common.models.Status;
import com.pomodoro.pomodoromate.participant.dtos.ParticipantSummaryDto;
import com.pomodoro.pomodoromate.participant.exceptions.ParticipantNotInRoomException;
import com.pomodoro.pomodoromate.studyRoom.exceptions.StudyRoomMismatchException;
import com.pomodoro.pomodoromate.studyRoom.models.StudyRoomId;
import com.pomodoro.pomodoromate.user.models.UserId;
import com.pomodoro.pomodoromate.user.models.UserInfo;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

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

    @Enumerated(EnumType.STRING)
    private Status status;

    private LocalDateTime joinedAt;

    @Builder
    public Participant(Long id, StudyRoomId studyRoomId, UserId userId, UserInfo userInfo, LocalDateTime joinedAt) {
        this.id = id;
        this.studyRoomId = studyRoomId;
        this.userId = userId;
        this.userInfo = userInfo;
        this.status = Status.ACTIVE;
        this.joinedAt = joinedAt;
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

    public void validateParticipant(UserId userId) {
        if (!this.userId.equals(userId)) {
            throw new AuthorizationException();
        }
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

    public void activate() {
        this.status = Status.ACTIVE;
    }

    public void pend() {
        this.status = Status.PENDING;
    }

    public void delete() {
        this.status = Status.DELETED;
    }

    public void validateActive() {
        if (!isActive()) {
            throw new ParticipantNotInRoomException();
        }
    }

    private boolean isActive() {
        return this.status.equals(Status.ACTIVE);
    }

    public boolean isDeleted() {
        return this.status.equals(Status.DELETED);
    }

    public boolean isPending() {
        return this.status.equals(Status.PENDING);
    }

    public boolean isHost(Long hostId) {
        return this.id.equals(hostId);
    }
}
