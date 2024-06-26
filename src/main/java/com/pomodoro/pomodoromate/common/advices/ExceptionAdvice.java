package com.pomodoro.pomodoromate.common.advices;

import com.pomodoro.pomodoromate.common.exceptions.CustomizedException;
import com.pomodoro.pomodoromate.common.exceptions.ExceptionResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j(topic = "ExceptionLogger")
@RestControllerAdvice
public class ExceptionAdvice {

    @ExceptionHandler(CustomizedException.class)
    public ResponseEntity<ExceptionResponse> customizedException(CustomizedException exception) {
        log.error("Error occurred: statusCode={}, message={}, stackTrace={}",
                exception.statusCode(),
                exception.message(),
                exception);
        return new ResponseEntity<>(ExceptionResponse.of(exception.message()), exception.statusCode());
    }
}
