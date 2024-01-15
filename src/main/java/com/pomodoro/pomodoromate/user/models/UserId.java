package com.pomodoro.pomodoromate.user.models;

import com.pomodoro.pomodoromate.common.models.EntityId;

public class UserId extends EntityId {
    public UserId() {
    }

    public UserId(Long value) {
        super(value);
    }

    public static UserId of(Long id) {
        return new UserId(id);
    }
}
