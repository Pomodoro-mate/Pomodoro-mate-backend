package com.pomodoro.pomodoromate.chat.repositories;

import com.pomodoro.pomodoromate.chat.models.Chat;
import com.pomodoro.pomodoromate.chat.models.QChat;
import com.pomodoro.pomodoromate.common.models.Status;
import com.pomodoro.pomodoromate.participant.models.QParticipant;
import com.pomodoro.pomodoromate.studyRoom.models.StudyRoomId;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class ChatMessageRepositoryImpl implements ChatMessageRepositoryQueryDsl {
    private final JPAQueryFactory queryFactory;

    public ChatMessageRepositoryImpl(JPAQueryFactory queryFactory) {
        this.queryFactory = queryFactory;
    }

    @Override
    public List<Chat> findAllBy(StudyRoomId studyRoomId) {
        QChat chat = QChat.chat;

        return queryFactory
                .select(chat)
                .from(chat)
                .where(chat.studyRoomId.eq(studyRoomId))
                .fetch();
    }
}
