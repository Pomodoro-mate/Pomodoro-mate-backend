package com.pomodoro.pomodoromate.user.models;

import com.pomodoro.pomodoromate.studyRoom.models.StudyRoomInfo;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.EqualsAndHashCode;

@Embeddable
@EqualsAndHashCode
public class UserInfo {
    @Column(name = "nickname")
    private String nickname;

    @Column(name = "imageUrl")
    private String imageUrl;

    @Column(name = "intro")
    private String intro;

    public UserInfo() {
    }

    public UserInfo(String nickname, String imageUrl, String intro) {
        this.nickname = nickname;
        this.imageUrl = imageUrl;
        this.intro = intro; //TODO 예외처리
    }

    public static StudyRoomInfo of(String name, String intro) {
        return new StudyRoomInfo(name, intro);
    }

    public String nickname() {
        return nickname;
    }

    public String imageUrl() {
        return imageUrl;
    }

    public String intro() {
        return intro;
    }
}
