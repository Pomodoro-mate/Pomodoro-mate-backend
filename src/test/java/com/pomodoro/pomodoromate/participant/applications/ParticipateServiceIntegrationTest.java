package com.pomodoro.pomodoromate.participant.applications;

import com.pomodoro.pomodoromate.participant.repositories.ParticipantRepository;
import com.pomodoro.pomodoromate.studyRoom.models.MaxParticipantCount;
import com.pomodoro.pomodoromate.studyRoom.models.StudyRoom;
import com.pomodoro.pomodoromate.studyRoom.repositories.StudyRoomRepository;
import com.pomodoro.pomodoromate.user.models.User;
import com.pomodoro.pomodoromate.user.models.UserId;
import com.pomodoro.pomodoromate.user.repositories.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ActiveProfiles("test")
class ParticipateServiceIntegrationTest {
    @Autowired
    private ParticipantRepository participantRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private StudyRoomRepository studyRoomRepository;

    @Autowired
    private ParticipateService participateService;

    @Test
    void participateConcurrentVerification() throws InterruptedException {
        int requestCount = 5;

        StudyRoom studyRoom = StudyRoom.builder()
                .id(1000L)
                .maxParticipantCount(MaxParticipantCount.of(3))
                .build();

        studyRoomRepository.save(studyRoom);

        for (int i = 1; i <= requestCount; i += 1) {
            User user = User.builder()
                    .id((long) i)
                    .build();

            userRepository.save(user);
        }

        int threadCount = 5;

        // thread 사용할 수 있는 서비스 선언, 몇 개의 스레드 사용할건지 지정
        ExecutorService executorService = Executors.newFixedThreadPool(threadCount);

        // 다른 스레드 작업 완료까지 기다리게 해주는 클래스
        // 몇을 카운트할지 지정
        // countDown()을 통해 0까지 세어야 await()하던 thread가 다시 실행됨
        CountDownLatch latch = new CountDownLatch(requestCount);

        for (int i = 1; i <= requestCount; i += 1) {
            UserId userId = UserId.of((long) i);
            executorService.submit(() -> {
                try {
                    participateService.participate(userId, studyRoom.id());
                } finally {
                    latch.countDown();
                }
            });
        }

        latch.await();

        Long participantCount = participantRepository.countActiveBy(studyRoom.id());

        assertThat(participantCount).isEqualTo(3L);
    }
}