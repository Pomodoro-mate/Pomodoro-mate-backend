package com.pomodoro.pomodoromate.studyRoom.controllers;

import com.pomodoro.pomodoromate.auth.config.JwtConfig;
import com.pomodoro.pomodoromate.auth.utils.JwtUtil;
import com.pomodoro.pomodoromate.common.dtos.PageDto;
import com.pomodoro.pomodoromate.config.SecurityConfig;
import com.pomodoro.pomodoromate.studyRoom.applications.CreateStudyRoomService;
import com.pomodoro.pomodoromate.studyRoom.applications.GetStudyRoomsService;
import com.pomodoro.pomodoromate.studyRoom.dtos.StudyRoomSummariesDto;
import com.pomodoro.pomodoromate.studyRoom.dtos.StudyRoomSummaryDto;
import com.pomodoro.pomodoromate.user.models.UserId;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.List;

import static org.hamcrest.CoreMatchers.containsString;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(StudyRoomController.class)
@Import({SecurityConfig.class, JwtConfig.class})
class StudyRoomControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CreateStudyRoomService createStudyRoomService;

    @MockBean
    private GetStudyRoomsService getStudyRoomsService;

    @SpyBean
    private JwtUtil jwtUtil;

    @Test
    void createStudyRoom() throws Exception {
        UserId userId = new UserId(1L);

        String token = jwtUtil.encode(userId);

        given(createStudyRoomService.create(any()))
                .willReturn(1L);

        mockMvc.perform(MockMvcRequestBuilders.post("/studyrooms")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{" +
                                "   \"name\": \"스터디\", " +
                                "   \"intro\": \"공부할 사람 구해요\"" +
//                                "   \"isPrivate\": \"false\", " +
//                                "   \"password\": \"\"" +
                                "}"))
                .andExpect(status().isCreated());
    }

    @Test
    void createStudyRoomWithBlankName() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/studyrooms")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{" +
                                "   \"name\": \"\", " +
                                "   \"intro\": \"공부할 사람 구해요\"" +
//                                "   \"isPrivate\": \"false\", " +
//                                "   \"password\": \"\"" +
                                "}"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void createStudyRoomWithInvalidNameUnder2() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/studyrooms")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{" +
                                "   \"name\": \"스\", " +
                                "   \"intro\": \"공부할 사람 구해요\"" +
//                                "   \"isPrivate\": \"false\", " +
//                                "   \"password\": \"\"" +
                                "}"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void createStudyRoomWithInvalidNameOver30() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/studyrooms")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{" +
                                "   \"name\": \"" + "스".repeat(31) + "\", " +
                                "   \"intro\": \"공부할 사람 구해요\"" +
//                                "   \"isPrivate\": \"false\", " +
//                                "   \"password\": \"\"" +
                                "}"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void studyRoom() throws Exception {
        StudyRoomSummariesDto studyRoomSummariesDto = StudyRoomSummariesDto.builder()
                .studyRooms(List.of(
                        StudyRoomSummaryDto.fake(1L, "스터디방 1"),
                        StudyRoomSummaryDto.fake(2L, "스터디방 2")
                ))
                .pageDto(new PageDto(1, 1))
                .build();

        given(getStudyRoomsService.studyRooms(1))
                .willReturn(studyRoomSummariesDto);

        mockMvc.perform(MockMvcRequestBuilders.get("/studyrooms"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString(
                        "\"id\":1"
                )));
    }
}
