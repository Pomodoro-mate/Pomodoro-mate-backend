package com.pomodoro.pomodoromate.studyRoom.models;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class StepTest {
    @Test
    void nextStep() {
        assertThat(Step.PLANNING.nextStep()).isEqualTo(Step.STUDYING);
        assertThat(Step.STUDYING.nextStep()).isEqualTo(Step.RETROSPECT);
        assertThat(Step.RETROSPECT.nextStep()).isEqualTo(Step.RESTING);
        assertThat(Step.RESTING.nextStep()).isEqualTo(Step.PLANNING);
    }
}
