package com.pomodoro.pomodoromate.user.models;

import com.pomodoro.pomodoromate.common.models.BaseEntity;
import com.pomodoro.pomodoromate.common.models.Status;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Builder;

@Entity
@Table(name = "users")
public class User extends BaseEntity {
    @Id
    @GeneratedValue
    private Long id;

    @Embedded
    private Username username;

    @Embedded
    private UserInfo info;

    @Enumerated(EnumType.STRING)
    private LoginType loginType;

    @Enumerated(EnumType.STRING)
    private Status status;

    public User() {
    }

    @Builder
    public User(Long id, Username username, UserInfo info, LoginType loginType) {
        this.id = id;
        this.username = username;
        this.info = info;
        this.loginType = loginType;
        this.status = Status.ACTIVE;
    }

    public static User guest(UserInfo userInfo) {
        return User.builder()
                .info(userInfo)
                .loginType(LoginType.GUEST)
                .build();
    }

    public UserId id() {
        return UserId.of(id);
    }

    public Username username() {
        return username;
    }

    public UserInfo info() {
        return info;
    }

    public LoginType loginType() {
        return loginType;
    }

    public Status status() {
        return status;
    }
}
