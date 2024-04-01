package com.pomodoro.pomodoromate.studyRoom.dtos;

import com.pomodoro.pomodoromate.studyRoom.models.StudyRoomInfo;
import com.pomodoro.pomodoromate.studyRoom.models.StudyRoomPassword;
import com.pomodoro.pomodoromate.studyRoom.models.StudyRoomStatus;
import com.pomodoro.pomodoromate.studyRoom.models.TimeSet;
import lombok.Builder;

public class CreateStudyRoomRequest {
    private StudyRoomInfo info;
    private TimeSet timeSet;
//    private StudyRoomPassword password;
//    private StudyRoomStatus status;

    @Builder
    public CreateStudyRoomRequest(StudyRoomInfo info, TimeSet timeSet) {
        this.info = info;
        this.timeSet = timeSet;
//        this.password = password;
//        this.status = status;
    }

    public static CreateStudyRoomRequest of(CreateStudyRoomRequestDto requestDto) {
        return new CreateStudyRoomRequest(
                new StudyRoomInfo(requestDto.name(), requestDto.intro()),
                new TimeSet(requestDto.timeSet().planningTime(),
                        requestDto.timeSet().studyingTime(),
                        requestDto.timeSet().retrospectTime(),
                        requestDto.timeSet().restingTime())
//                new StudyRoomPassword(requestDto.password()),
//                requestDto.isPrivate() ? StudyRoomStatus.PRIVATE :
//                        requestDto.password().length() == 0 ? StudyRoomStatus.NORMAL :
//                                StudyRoomStatus.PASSWORD_PROTECTED
        );
    }

    public StudyRoomInfo getInfo() {
        return info;
    }

    public TimeSet getTimeSet() {
        return timeSet;
    }

    //    public StudyRoomPassword getPassword() {
//        return password;
//    }
//
//    public StudyRoomStatus getStatus() {
//        return status;
//    }
}