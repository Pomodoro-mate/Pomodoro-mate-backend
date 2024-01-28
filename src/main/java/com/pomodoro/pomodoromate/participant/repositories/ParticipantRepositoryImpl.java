package com.pomodoro.pomodoromate.participant.repositories;

import com.pomodoro.pomodoromate.common.models.Status;
import com.pomodoro.pomodoromate.participant.models.QParticipant;
import com.pomodoro.pomodoromate.studyRoom.models.StudyRoomId;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.stereotype.Repository;

@Repository
public class ParticipantRepositoryImpl implements ParticipantRepositoryQueryDsl{
    private final JPAQueryFactory queryFactory;

    public ParticipantRepositoryImpl(JPAQueryFactory queryFactory) {
        this.queryFactory = queryFactory;
    }

    @Override
    public Long countActiveByStudyRoomId(StudyRoomId studyRoomId) {
        QParticipant participant = QParticipant.participant;

        return queryFactory
                .select(participant.count())
                .from(participant)
                .where(participant.studyRoomId.eq(studyRoomId).and(
                        participant.status.eq(Status.ACTIVE)))
                .fetchOne();
    }
}
