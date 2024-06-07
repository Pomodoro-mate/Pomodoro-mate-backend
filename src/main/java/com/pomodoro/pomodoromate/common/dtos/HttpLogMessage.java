package com.pomodoro.pomodoromate.common.dtos;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Builder;
import org.springframework.web.util.ContentCachingRequestWrapper;
import org.springframework.web.util.ContentCachingResponseWrapper;

import java.io.IOException;


@Builder
public record HttpLogMessage(
        ContentCachingRequestWrapper request,
        ContentCachingResponseWrapper response,
        long elapsedTime,
        ObjectMapper objectMapper
) {
    private static final String HTTP_LOG_FORMAT = """
                    
            Request:
                RequestURI: %s %s
                QueryString: %s
                Authorization: %s
                Body: %s
            ================
            Response:
                StatusCode: %d
                Body: %s
                ElapsedTime: %d ms
                    """;

    public String toFormattedLog() throws IOException {
        String requestBody = new String(request.getContentAsByteArray(), request.getCharacterEncoding());
        String responseBody = new String(response.getContentAsByteArray(), response.getCharacterEncoding());

        return String.format(
                HTTP_LOG_FORMAT,
                request.getMethod(),
                request.getRequestURI(),
                request.getQueryString(),
                request.getHeader("Authorization"),
                formatJson(requestBody),

                response.getStatus(),
                formatJson(responseBody),
                elapsedTime
        );
    }

    private String formatJson(String jsonString) throws IOException {
        if (jsonString == null || jsonString.isBlank()) {
            return "";
        }

        Object jsonObject = objectMapper.readValue(jsonString, Object.class);
        return objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(jsonObject);
    }
}
