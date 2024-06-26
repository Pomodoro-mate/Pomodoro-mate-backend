package com.pomodoro.pomodoromate.studyRoom.models;

import com.pomodoro.pomodoromate.participant.dtos.ParticipantSummaryDto;
import com.pomodoro.pomodoromate.participant.exceptions.ForbiddenStudyHostActionException;
import com.pomodoro.pomodoromate.participant.models.ParticipantId;
import com.pomodoro.pomodoromate.studyRoom.dtos.NextStepStudyRoomDto;
import com.pomodoro.pomodoromate.studyRoom.dtos.StudyRoomDetailDto;
import com.pomodoro.pomodoromate.studyRoom.exceptions.HostExistsException;
import com.pomodoro.pomodoromate.studyRoom.exceptions.InvalidStepException;
import com.pomodoro.pomodoromate.studyRoom.exceptions.MaxParticipantExceededException;
import com.pomodoro.pomodoromate.studyRoom.exceptions.StudyAlreadyCompletedException;
import javax.persistence.GenerationType;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EntityListeners(AuditingEntityListener.class)
public class StudyRoom {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Embedded
    private StudyRoomInfo info;

    @AttributeOverride(name = "value", column = @Column(name = "hostId"))
    private ParticipantId hostId;

//    private StudyRoomPassword password;

    @Embedded
    private MaxParticipantCount maxParticipantCount;

    @Embedded
    private TimeSet timeSet;

    @Enumerated(EnumType.STRING)
    private Step step;

    @Column(updatable = false)
    @CreatedDate
    private LocalDateTime createAt;

    private LocalDateTime updateAt;

    @Builder
    public StudyRoom(Long id, StudyRoomInfo info, MaxParticipantCount maxParticipantCount, TimeSet timeSet) {
        this.id = id;
        this.info = info;
//        this.status = status;
        this.maxParticipantCount = maxParticipantCount;
        this.timeSet = timeSet;

        this.step = Step.WAITING;
    }


//    public void changePassword(StudyRoomPassword password, PasswordEncoder passwordEncoder) {
//        Pattern pattern = Pattern.compile(PASSWORD_PATTERN);
//        Matcher matcher = pattern.matcher(password.getValue());
//
//        if (!matcher.find()) {
//            throw new InvalidStudyRoomPasswordException();
//        }
//
//        this.password = StudyRoomPassword.of(passwordEncoder.encode(password.getValue()));
//    }
//
//    public void authenticate(StudyRoomPassword password, PasswordEncoder passwordEncoder) {
//        if (!passwordEncoder.matches(password.getValue(), this.password.getValue())) {
//            throw new IncorrectStudyRoomPasswordException();
//        }

    //    }
    public void validateIncomplete() {
        if (isStep(Step.COMPLETED)) {
            throw new StudyAlreadyCompletedException();
        }
    }

    private boolean isStep(Step step) {
        return this.step == step;
    }

    public void complete() {
        this.step = Step.COMPLETED;
    }

    public void validateCurrentStep(Step step) {
        if (!isStep(step)) {
            throw new InvalidStepException();
        }
    }

    public void proceedToNextStep() {
        this.step = step.nextStep();
        this.updateAt = LocalDateTime.now();

        if (timeSet.isTimeZero(step)) {
            proceedToNextStep();
        }
    }

    public void validateMaxParticipantExceeded(Long participantCount) {
        if (isMaxParticipantExceeded(participantCount)) {
            throw new MaxParticipantExceededException();
        }
    }

    private boolean isMaxParticipantExceeded(Long participantCount) {
        return participantCount >= maxParticipantCount.value();
    }

    public StudyRoomId id() {
        return StudyRoomId.of(id);
    }

    public StudyRoomInfo info() {
        return info;
    }

    public ParticipantId hostId() {
        return hostId;
    }

//    public StudyRoomPassword password() {
//        return password;

    //    }
    public Step step() {
        return step;
    }

    public TimeSet timeSet() {
        return timeSet;
    }

    public LocalDateTime createAt() {
        return createAt;
    }

    public LocalDateTime updateAt() {
        return updateAt;
    }

    public StudyRoomDetailDto toDetailDto(List<ParticipantSummaryDto> participantSummaryDtos) {
        return new StudyRoomDetailDto(id, info().name(), info.intro(),
                step.toString(), timeSet.toDto(), participantSummaryDtos, updateAt());
    }

    public Integer maxParticipantCount() {
        return maxParticipantCount.value();
    }

    public NextStepStudyRoomDto toNextStepDto() {
        return new NextStepStudyRoomDto(id, step.toString(), timeSet.getTimeOf(step), updateAt());
    }

    public void update(StudyRoomInfo info, TimeSet timeSet) {
        this.info = info;
        this.timeSet = timeSet;
    }

    public void assignHost(ParticipantId participantId) {
        this.hostId = participantId;
    }

    public void validateHost(ParticipantId hostId) {
        if (!this.hostId.value().equals(hostId.value())) {
            throw new ForbiddenStudyHostActionException();
        }
    }

    public void excludeHost() {
        this.hostId = null;
    }

    public void checkHostExists() {
        if (hostId() != null) {
            throw new HostExistsException();
        }
    }
}
