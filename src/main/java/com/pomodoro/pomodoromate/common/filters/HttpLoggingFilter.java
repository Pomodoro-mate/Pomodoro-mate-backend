package com.pomodoro.pomodoromate.common.filters;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pomodoro.pomodoromate.common.dtos.HttpLogMessage;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.ContentCachingRequestWrapper;
import org.springframework.web.util.ContentCachingResponseWrapper;

import java.io.IOException;
import java.util.UUID;

@Slf4j(topic = "HttpLogger")
public class HttpLoggingFilter extends OncePerRequestFilter {
    private static final String REQUEST_ID = "request_id";

    private final ObjectMapper objectMapper;

    public HttpLoggingFilter(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        String requestId = UUID.randomUUID().toString().substring(0, 8);
        MDC.put(REQUEST_ID, requestId);

        ContentCachingRequestWrapper cachingRequest = new ContentCachingRequestWrapper(request);
        ContentCachingResponseWrapper cachingResponse = new ContentCachingResponseWrapper(response);

        long startTime = System.currentTimeMillis();
        filterChain.doFilter(cachingRequest, cachingResponse);
        long endTime = System.currentTimeMillis();

        try {
            log.info(
                    HttpLogMessage.builder()
                            .request(cachingRequest)
                            .response(cachingResponse)
                            .elapsedTime(endTime - startTime)
                            .objectMapper(objectMapper)
                            .build()
                            .toFormattedLog()
            );

            cachingResponse.copyBodyToResponse();
        } catch (Exception exception) {
            log.error("[{}] Logging 실패", this.getClass().getSimpleName(), exception);
        }

        MDC.clear();
    }
}
