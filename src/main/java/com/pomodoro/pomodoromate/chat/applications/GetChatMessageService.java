package com.pomodoro.pomodoromate.chat.applications;

import com.pomodoro.pomodoromate.chat.dtos.ChatRequestDto;
import com.pomodoro.pomodoromate.chat.dtos.ChatSummariesDto;
import com.pomodoro.pomodoromate.chat.dtos.ChatSummaryDto;
import com.pomodoro.pomodoromate.chat.models.Chat;
import com.pomodoro.pomodoromate.chat.models.Message;
import com.pomodoro.pomodoromate.chat.models.Writer;
import com.pomodoro.pomodoromate.chat.repositories.ChatMessageRepository;
import com.pomodoro.pomodoromate.participant.models.ParticipantId;
import com.pomodoro.pomodoromate.studyRoom.models.StudyRoomId;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class GetChatMessageService {
    private final ChatMessageRepository chatMessageRepository;
    private final SimpMessagingTemplate template;

    public GetChatMessageService(
            ChatMessageRepository chatMessageRepository,
            SimpMessagingTemplate template) {
        this.chatMessageRepository = chatMessageRepository;
        this.template = template;
    }

    @Transactional(readOnly = true)
    public void getChatsForEntry(StudyRoomId studyRoomId) {
        List<Chat> chats = chatMessageRepository.findAllBy(studyRoomId);

        List<ChatSummaryDto> chatSummaryDtos = chats.stream()
                .map(Chat::toSummaryDto).toList();

        template.convertAndSend("/sub/user/chat", new ChatSummariesDto(chatSummaryDtos));
    }
}
