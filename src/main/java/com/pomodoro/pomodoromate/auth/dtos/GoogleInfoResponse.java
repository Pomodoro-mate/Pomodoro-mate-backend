package com.pomodoro.pomodoromate.auth.dtos;

public record GoogleInfoResponse(
        String iss,
        String aud,
        String sub,
        String email,
        String email_verified,
        String name,
        String picture,
        String given_name,
        String family_name
) {
}
