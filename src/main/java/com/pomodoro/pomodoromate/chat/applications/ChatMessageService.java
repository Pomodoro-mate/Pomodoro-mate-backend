package com.pomodoro.pomodoromate.chat.applications;

import com.pomodoro.pomodoromate.chat.dtos.ChatRequestDto;
import com.pomodoro.pomodoromate.chat.dtos.ChatSummaryDto;
import com.pomodoro.pomodoromate.chat.models.Chat;
import com.pomodoro.pomodoromate.chat.models.Message;
import com.pomodoro.pomodoromate.chat.models.Writer;
import com.pomodoro.pomodoromate.chat.repositories.ChatRepository;
import com.pomodoro.pomodoromate.participant.models.ParticipantId;
import com.pomodoro.pomodoromate.studyRoom.models.StudyRoomId;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ChatMessageService {
    private final ChatRepository chatRepository;
    private final SimpMessagingTemplate template;

    public ChatMessageService(
            ChatRepository chatRepository,
            SimpMessagingTemplate template) {
        this.chatRepository = chatRepository;
        this.template = template;
    }

    @Transactional
    public void sendMessage(ChatRequestDto chatRequestDto) {
        Chat chat = Chat.builder()
                .studyRoomId(StudyRoomId.of(chatRequestDto.studyRoomId()))
                .participantId(ParticipantId.of(chatRequestDto.participantId()))
                .writer(Writer.of(chatRequestDto.writer()))
                .message(Message.of(chatRequestDto.message()))
                .build();

        Chat saved = chatRepository.save(chat);

        ChatSummaryDto chatSummaryDto = saved.toSummaryDto();

        template.convertAndSend("/sub/studyRooms/" + chatSummaryDto.roomId() + "/chat", chatSummaryDto);
    }
}
