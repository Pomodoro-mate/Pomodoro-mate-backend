package com.pomodoro.pomodoromate.studyRoom.dtos;

import com.pomodoro.pomodoromate.studyRoom.models.StudyRoomInfo;
import com.pomodoro.pomodoromate.studyRoom.models.StudyRoomPassword;
import com.pomodoro.pomodoromate.studyRoom.models.StudyRoomStatus;

public class CreateStudyRoomRequest {
    private StudyRoomInfo info;
//    private StudyRoomPassword password;
//    private StudyRoomStatus status;

    public CreateStudyRoomRequest(StudyRoomInfo info) {
        this.info = info;
//        this.password = password;
//        this.status = status;
    }

    public static CreateStudyRoomRequest of(CreateStudyRoomRequestDto requestDto) {
        return new CreateStudyRoomRequest(
                new StudyRoomInfo(requestDto.name(), requestDto.intro())
//                new StudyRoomPassword(requestDto.password()),
//                requestDto.isPrivate() ? StudyRoomStatus.PRIVATE :
//                        requestDto.password().length() == 0 ? StudyRoomStatus.NORMAL :
//                                StudyRoomStatus.PASSWORD_PROTECTED
        );
    }

    public StudyRoomInfo getInfo() {
        return info;
    }

//    public StudyRoomPassword getPassword() {
//        return password;
//    }
//
//    public StudyRoomStatus getStatus() {
//        return status;
//    }
}
