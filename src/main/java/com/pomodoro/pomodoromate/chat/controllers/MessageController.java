package com.pomodoro.pomodoromate.chat.controllers;

import com.pomodoro.pomodoromate.chat.applications.ChatMessageService;
import com.pomodoro.pomodoromate.chat.dtos.ChatRequestDto;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MessageController {
    private final ChatMessageService chatMessageService;

    public MessageController(
            ChatMessageService chatMessageService
    ) {
        this.chatMessageService = chatMessageService;
    }

    @MessageMapping("/user/chat/message")
    public void sendMessage(
            ChatRequestDto chatRequestDto
    ) {
        chatMessageService.sendMessage(chatRequestDto);
    }
}
