package com.pomodoro.pomodoromate.common.repositories;

import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import org.springframework.stereotype.Component;

@Component
public class LockTimeoutRepository {
    private final EntityManager entityManager;

    public LockTimeoutRepository(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    public void setLockTimeout(int timeout) {
        Query query = entityManager.createNativeQuery("SET lock_timeout = " + timeout);
        query.executeUpdate();
    }
}
