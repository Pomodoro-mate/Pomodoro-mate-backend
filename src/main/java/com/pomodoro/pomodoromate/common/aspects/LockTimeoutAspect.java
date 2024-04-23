package com.pomodoro.pomodoromate.common.aspects;

import com.pomodoro.pomodoromate.common.repositories.LockTimeoutRepository;
import com.pomodoro.pomodoromate.common.annotations.LockTimeout;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

@Aspect
@Component
@RequiredArgsConstructor
public class LockTimeoutAspect {
    private final LockTimeoutRepository lockTimeoutRepository;

    @Before("@annotation(com.pomodoro.pomodoromate.common.annotations.LockTimeout)")
    public void beforeLockTimeout(JoinPoint joinPoint) {
        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        LockTimeout lockTimeout = methodSignature.getMethod().getAnnotation(LockTimeout.class);
        lockTimeoutRepository.setLockTimeout(lockTimeout.timeout());
    }
}
