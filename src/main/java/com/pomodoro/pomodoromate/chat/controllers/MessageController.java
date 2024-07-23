package com.pomodoro.pomodoromate.chat.controllers;

import com.pomodoro.pomodoromate.auth.utils.JwtUtil;
import com.pomodoro.pomodoromate.chat.applications.ChatMessageService;
import com.pomodoro.pomodoromate.chat.dtos.ChatRequestDto;
import com.pomodoro.pomodoromate.chat.dtos.ChatSummariesDto;
import com.pomodoro.pomodoromate.chat.dtos.ChatSummaryDto;
import com.pomodoro.pomodoromate.studyRoom.models.StudyRoomId;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MessageController {
    private final SimpMessagingTemplate template;
    private final ChatMessageService chatMessageService;
    private final JwtUtil jwtUtil;

    public MessageController(SimpMessagingTemplate template, ChatMessageService chatMessageService, JwtUtil jwtUtil) {
        this.template = template;
        this.chatMessageService = chatMessageService;
        this.jwtUtil = jwtUtil;
    }

    @MessageMapping("/user/chat/enter")
    public void userEnter(
    ) {
        StudyRoomId studyRoomId = new StudyRoomId(1L);

        ChatSummariesDto chatSummariesDto = chatMessageService.findChats(studyRoomId);

        template.convertAndSend("/sub/user/chat", chatSummariesDto);
    }


    @MessageMapping("/user/chat/message")
    public void message(
            ChatRequestDto chatRequestDto
    ) {
        ChatSummaryDto chatSummaryDto = chatMessageService.save(chatRequestDto);

        template.convertAndSend("/sub/chat/room/" + chatSummaryDto.roomId(), chatSummaryDto);
    }
}
