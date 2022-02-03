package com.example.twitterclone.controller;

import com.example.twitterclone.dto.BoardDTO;
import com.example.twitterclone.entity.Board;
import com.example.twitterclone.repository.BoardRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@SpringBootTest
@Transactional
class BoardApiControllerTest {
    @Autowired private MockMvc mvc;
    @Autowired private ObjectMapper mapper;
    @Mock private BoardRepository boardRepository;
    @InjectMocks private BoardApiController boardApiController;

    @Test
    @DisplayName("작성 성공")
    void save() throws Exception {
        //given
        BoardDTO boardDTO = new BoardDTO();
        boardDTO.setPassword("pwd1234");
        boardDTO.setContent("내용");
        String content = mapper.writeValueAsString(boardDTO);

        //when //then
        mvc.perform(
                post("/api/board")
                .content(content)
                .contentType(MediaType.APPLICATION_JSON)
        )
        .andExpect(status().isOk())
        .andDo(print());
    }


    @Test
    @DisplayName("작성 실패 - 빈칸 ")
    void saveBlankFail() throws Exception {
        //given
        BoardDTO boardDTO = new BoardDTO();
        boardDTO.setPassword("");
        boardDTO.setContent("");
        String content = mapper.writeValueAsString(boardDTO);

        //when //then
        mvc.perform(
                post("/api/board")
                .content(content)
                .contentType(MediaType.APPLICATION_JSON)
        )
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$[?(@.field=='password')]defaultMessage").value("비밀번호를 입력해주세요."))
        .andExpect(jsonPath("$[?(@.field=='content')]defaultMessage").value("내용을 입력해주세요."))
        .andDo(print());

        /*
        * [
           {
              "field" : "content",
              "objectName" : "boardDTO",
              "code" : "NotBlank",
              "defaultMessage" : "내용을 입력해주세요.",
              "rejectedValue" : ""
           },
           {
              "field" : "password",
              "objectName" : "boardDTO",
              "code" : "NotBlank",
              "defaultMessage" : "비밀번호를 입력해주세요.",
              "rejectedValue" : ""
           }
        ]*/
    }

    @Test
    @DisplayName("작성 실패 - 내용 100자 이상")
    void saveMaxSizeContentFail() throws Exception {
        //given
        BoardDTO boardDTO = new BoardDTO();
        boardDTO.setPassword("pwd1313");
        boardDTO.setContent("dddddddddd"+"dddddddddd"+"dddddddddd"+"dddddddddd"+"dddddddddd"+
                            "dddddddddd"+"dddddddddd"+"dddddddddd"+"dddddddddd"+"dddddddddd"+"dddddddddd");
        String content = mapper.writeValueAsString(boardDTO);

        //when //then
        mvc.perform(
                post("/api/board")
                .content(content)
                .contentType(MediaType.APPLICATION_JSON)
        )
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$[?(@.field=='content')]defaultMessage").value("100자 이하로 입력해주세요."))
        .andDo(print());
    }

}