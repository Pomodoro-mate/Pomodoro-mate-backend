package com.pomodoro.pomodoromate.user.models;

import com.pomodoro.pomodoromate.user.exceptions.InvalidUserNicknameException;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.EqualsAndHashCode;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.pomodoro.pomodoromate.user.policies.UserPolicy.NICKNAME_PATTERN;

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

    public UserInfo(String nickname) {
        validateNickName(nickname);

        this.nickname = nickname;
    }

    public UserInfo(String nickname, String imageUrl, String intro) {
        this.nickname = nickname;
        this.imageUrl = imageUrl;
        this.intro = intro; //TODO 예외처리
    }

    private void validateNickName(String nickname) {
        Pattern pattern = Pattern.compile(NICKNAME_PATTERN);
        Matcher matcher = pattern.matcher(nickname);

        if (!matcher.find()) {
            throw new InvalidUserNicknameException();
        }
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
