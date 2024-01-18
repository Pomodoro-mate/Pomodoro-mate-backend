package com.pomodoro.pomodoromate.auth.dtos;

import com.pomodoro.pomodoromate.user.models.UserInfo;
import lombok.Builder;

public class CreateGuestRequest {
    UserInfo userInfo;

    @Builder
    public CreateGuestRequest(UserInfo userInfo) {
        this.userInfo = userInfo;
    }

    public static CreateGuestRequest of(CreateGuestRequestDto requestDto) {
        return new CreateGuestRequest(
                new UserInfo(requestDto.nickname())
        );
    }

    public UserInfo getUserInfo() {
        return userInfo;
    }
}
