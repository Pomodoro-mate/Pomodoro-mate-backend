package com.pomodoro.pomodoromate.common.advices;

import com.pomodoro.pomodoromate.common.exceptions.CustomizedException;
import com.pomodoro.pomodoromate.common.exceptions.ExceptionResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ExceptionAdvice {

    @ExceptionHandler(CustomizedException.class)
    public ResponseEntity<ExceptionResponse> customizedException(CustomizedException exception) {
        return new ResponseEntity<>(ExceptionResponse.of(exception.message()), exception.statusCode());
    }
}
