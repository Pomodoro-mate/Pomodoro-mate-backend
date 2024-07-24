package com.pomodoro.pomodoromate.chat.controllers;

import com.pomodoro.pomodoromate.chat.applications.ChatMessageService;
import com.pomodoro.pomodoromate.chat.applications.GetChatMessageService;
import com.pomodoro.pomodoromate.chat.dtos.ChatRequestDto;
import com.pomodoro.pomodoromate.studyRoom.models.StudyRoomId;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MessageController {
    private final GetChatMessageService getChatMessageService;
    private final ChatMessageService chatMessageService;

    public MessageController(
            ChatMessageService chatMessageService,
            GetChatMessageService getChatMessageService
    ) {
        this.getChatMessageService = getChatMessageService;
        this.chatMessageService = chatMessageService;
    }

    @MessageMapping("/user/chat/enter")
    public void userEnter(
    ) {
        StudyRoomId studyRoomId = new StudyRoomId(1L);

        getChatMessageService.getChatsForEntry(studyRoomId);
    }


    @MessageMapping("/user/chat/message")
    public void message(
            ChatRequestDto chatRequestDto
    ) {
        chatMessageService.sendMessage(chatRequestDto);
    }
}
