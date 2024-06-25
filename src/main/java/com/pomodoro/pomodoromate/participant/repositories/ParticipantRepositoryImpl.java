package com.pomodoro.pomodoromate.participant.repositories;

import com.pomodoro.pomodoromate.common.models.SessionId;
import com.pomodoro.pomodoromate.common.models.Status;
import com.pomodoro.pomodoromate.participant.models.Participant;
import com.pomodoro.pomodoromate.participant.models.ParticipantId;
import com.pomodoro.pomodoromate.participant.models.QParticipant;
import com.pomodoro.pomodoromate.studyRoom.models.StudyRoomId;
import com.pomodoro.pomodoromate.user.models.UserId;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class ParticipantRepositoryImpl implements ParticipantRepositoryQueryDsl{
    private final JPAQueryFactory queryFactory;

    public ParticipantRepositoryImpl(JPAQueryFactory queryFactory) {
        this.queryFactory = queryFactory;
    }

    @Override
    public Long countNotDeletedBy(StudyRoomId studyRoomId) {
        QParticipant participant = QParticipant.participant;

        return queryFactory
                .select(participant.count())
                .from(participant)
                .where(participant.studyRoomId.eq(studyRoomId).and(
                        participant.status.ne(Status.DELETED)))
                .fetchOne();
    }

    @Override
    public List<Participant> findAllNotDeletedBy(StudyRoomId studyRoomId) {
        QParticipant participant = QParticipant.participant;

        return queryFactory
                .select(participant)
                .from(participant)
                .where(participant.studyRoomId.eq(studyRoomId).and(
                        participant.status.ne(Status.DELETED)))
                .fetch();
    }

    @Override
    public Optional<Participant> findBy(UserId userId, StudyRoomId studyRoomId) {
        QParticipant participant = QParticipant.participant;

        return Optional.ofNullable(queryFactory
                .selectFrom(participant)
                .where(participant.studyRoomId.eq(studyRoomId).and(
                        participant.userId.eq(userId)))
                .fetchOne());
    }

    @Override
    public Optional<Participant> findBy(ParticipantId participantId) {
        QParticipant participant = QParticipant.participant;

        return Optional.ofNullable(queryFactory
                .selectFrom(participant)
                .where(participant.id.eq(participantId.value()))
                .fetchOne());
    }

    @Override
    public Optional<Participant> findMostRecentBy(StudyRoomId studyRoomId) {
        QParticipant participant = QParticipant.participant;

        return Optional.ofNullable(queryFactory
                .selectFrom(participant)
                .where(participant.studyRoomId.eq(studyRoomId)
                        .and(participant.status.ne(Status.DELETED)))
                .orderBy(participant.joinedAt.asc())
                .fetchFirst());
    }
}
