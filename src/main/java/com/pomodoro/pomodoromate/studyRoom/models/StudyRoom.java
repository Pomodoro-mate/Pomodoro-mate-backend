package com.pomodoro.pomodoromate.studyRoom.models;

import com.pomodoro.pomodoromate.common.models.BaseEntity;
import com.pomodoro.pomodoromate.participant.dtos.ParticipantSummaryDto;
import com.pomodoro.pomodoromate.studyRoom.dtos.StudyRoomDetailDto;
import com.pomodoro.pomodoromate.studyRoom.exceptions.InvalidStepException;
import com.pomodoro.pomodoromate.studyRoom.exceptions.MaxParticipantExceededException;
import com.pomodoro.pomodoromate.studyRoom.exceptions.StudyAlreadyCompletedException;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class StudyRoom extends BaseEntity {
    @Id
    @GeneratedValue
    private Long id;

    @Embedded
    private StudyRoomInfo info;

//    @Column(name = "hostId")
//    private UserId hostId;

//    private StudyRoomPassword password;

    @Embedded
    private MaxParticipantCount maxParticipantCount;

    @Enumerated(EnumType.STRING)
    private Step step;

    @Builder
    public StudyRoom(Long id, StudyRoomInfo info, MaxParticipantCount maxParticipantCount) {
        this.id = id;
        this.info = info;
//        this.hostId = hostId;
//        this.status = status;
        this.maxParticipantCount = maxParticipantCount;

        this.step = Step.PLANNING;
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
//    public UserId hostId() {
//        return hostId;

//    }
//    public StudyRoomPassword password() {
//        return password;

//    }

    public Step step() {
        return step;
    }

    public StudyRoomDetailDto toDetailDto(List<ParticipantSummaryDto> participantSummaryDtos) {
        return new StudyRoomDetailDto(id, info().name(), info.intro(),
                step.toString(), participantSummaryDtos, updateAt());
    }

    public Integer maxParticipantCount() {
        return maxParticipantCount.value();
    }
}
