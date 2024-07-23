package com.pomodoro.pomodoromate.chat.applications;

import com.pomodoro.pomodoromate.chat.dtos.ChatRequestDto;
import com.pomodoro.pomodoromate.chat.dtos.ChatSummariesDto;
import com.pomodoro.pomodoromate.chat.dtos.ChatSummaryDto;
import com.pomodoro.pomodoromate.chat.models.Chat;
import com.pomodoro.pomodoromate.chat.models.Message;
import com.pomodoro.pomodoromate.chat.models.Writer;
import com.pomodoro.pomodoromate.chat.repositories.ChatMessageRepository;
import com.pomodoro.pomodoromate.participant.models.Participant;
import com.pomodoro.pomodoromate.participant.models.ParticipantId;
import com.pomodoro.pomodoromate.studyRoom.models.StudyRoomId;
import com.pomodoro.pomodoromate.user.models.UserId;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class ChatMessageService {
    private final ChatMessageRepository chatMessageRepository;

    public ChatMessageService(ChatMessageRepository chatMessageRepository) {
        this.chatMessageRepository = chatMessageRepository;
    }

    public ChatSummariesDto findChats(StudyRoomId studyRoomId) {
        List<Chat> chats = chatMessageRepository.findAllBy(studyRoomId);

        List<ChatSummaryDto> chatSummaryDtos = chats.stream()
                .map(Chat::toSummaryDto).toList();

        return new ChatSummariesDto(chatSummaryDtos);
    }

    public ChatSummaryDto save(ChatRequestDto chatRequestDto) {
        Chat chat = Chat.builder()
                .studyRoomId(StudyRoomId.of(chatRequestDto.studyRoomId()))
                .participantId(ParticipantId.of(chatRequestDto.participantId()))
                .writer(Writer.of(chatRequestDto.writer()))
                .message(Message.of(chatRequestDto.message()))
                .build();

        Chat saved = chatMessageRepository.save(chat);

        return saved.toSummaryDto();
    }
}
