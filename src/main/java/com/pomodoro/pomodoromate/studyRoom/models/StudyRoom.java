package com.pomodoro.pomodoromate.studyRoom.models;

import com.pomodoro.pomodoromate.common.models.BaseEntity;
import com.pomodoro.pomodoromate.studyRoom.dtos.StudyRoomSummaryDto;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Builder;

@Entity
public class StudyRoom extends BaseEntity {
    @Id
    @GeneratedValue
    private Long id;

    @Enumerated
    private StudyRoomInfo info;

//    @Enumerated
//    @Column(name = "hostId")
//    private UserId hostId;

//    @Enumerated(EnumType.STRING)
//    private StudyRoomStatus status;
//
//    @Enumerated
//    private StudyRoomPassword password;

    @Enumerated(EnumType.STRING)
    private Step step;

    public StudyRoom() {
    }

    @Builder
    public StudyRoom(Long id, StudyRoomInfo info) {
        this.id = id;
        this.info = info;
//        this.hostId = hostId;
//        this.status = status;

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

    public Long id() {
        return id;
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

    public StudyRoomSummaryDto toSummaryDto() {
        return new StudyRoomSummaryDto(id, info().name(), info.intro(), step.toString());
    }
}
