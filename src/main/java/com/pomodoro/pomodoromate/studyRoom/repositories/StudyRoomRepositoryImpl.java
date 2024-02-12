package com.pomodoro.pomodoromate.studyRoom.repositories;

import com.pomodoro.pomodoromate.common.models.Status;
import com.pomodoro.pomodoromate.participant.models.QParticipant;
import com.pomodoro.pomodoromate.studyRoom.dtos.StudyRoomSummaryDto;
import com.pomodoro.pomodoromate.studyRoom.models.QStudyRoom;
import com.pomodoro.pomodoromate.studyRoom.models.Step;
import com.pomodoro.pomodoromate.studyRoom.models.StudyRoomId;
import com.querydsl.core.types.Expression;
import com.querydsl.core.types.ExpressionUtils;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class StudyRoomRepositoryImpl implements StudyRoomRepositoryQueryDsl {
    private final JPAQueryFactory queryFactory;

    public StudyRoomRepositoryImpl(JPAQueryFactory queryFactory) {
        this.queryFactory = queryFactory;
    }

    @Override
    public Page<StudyRoomSummaryDto> findAllSummaryDto(Pageable pageable) {
        QStudyRoom studyRoom = QStudyRoom.studyRoom;

        List<StudyRoomSummaryDto> studyRooms = queryFactory
                .select(Projections.constructor(StudyRoomSummaryDto.class,
                        studyRoom.id,
                        studyRoom.info.name,
                        studyRoom.info.intro,
                        studyRoom.step,
                        getActiveParticipantCount(studyRoom)
                ))
                .where(studyRoom.step.ne(Step.COMPLETED))
                .orderBy(studyRoom.createAt.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        Long count = getPageCount(studyRoom);

        return new PageImpl<>(studyRooms, pageable, count);
    }

    private Expression<Long> getActiveParticipantCount(QStudyRoom studyRoom) {
        QParticipant participant = QParticipant.participant;

        return ExpressionUtils.as(
                JPAExpressions.select(ExpressionUtils.count(participant.id))
                        .from(participant)
                        .where(participant.studyRoomId.value.eq(studyRoom.id).and(
                                participant.status.eq(Status.ACTIVE)
                        )),
                "participantCount"
        );
    }

    private Long getPageCount(QStudyRoom studyRoom) {
        return queryFactory
                .select(studyRoom.count())
                .from(studyRoom)
                .where(studyRoom.step.ne(Step.COMPLETED))
                .fetchOne();
    }
}
