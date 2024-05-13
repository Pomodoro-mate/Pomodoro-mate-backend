package com.pomodoro.pomodoromate.auth.dtos;

import lombok.Builder;

@Builder
public record GoogleRequest(
        String clientId,
        String redirectUri,
        String clientSecret,
        String responseType,
        String scope,
        String code,
        String accessType,
        String grantType,
        String state,
        String includeGrantedScopes,
        String loginHint,
        String prompt
) {
}
