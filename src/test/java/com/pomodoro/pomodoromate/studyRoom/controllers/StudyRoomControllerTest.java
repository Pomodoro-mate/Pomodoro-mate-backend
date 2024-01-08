package com.pomodoro.pomodoromate.studyRoom.controllers;

import com.pomodoro.pomodoromate.config.SecurityConfig;
import com.pomodoro.pomodoromate.studyRoom.applications.CreateStudyRoomService;
import com.pomodoro.pomodoromate.studyRoom.repositories.StudyRoomRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(StudyRoomController.class)
@Import({SecurityConfig.class})
class StudyRoomControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CreateStudyRoomService createStudyRoomService;

    @Test
    void createStudyRoom() throws Exception {
        given(createStudyRoomService.create(any()))
                .willReturn(1L);

        mockMvc.perform(MockMvcRequestBuilders.post("/studyrooms")
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
}
