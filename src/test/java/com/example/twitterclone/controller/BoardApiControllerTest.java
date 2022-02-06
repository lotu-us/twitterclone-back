package com.example.twitterclone.controller;

import com.example.twitterclone.dto.BoardDTO;
import com.example.twitterclone.entity.Board;
import com.example.twitterclone.repository.BoardRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@SpringBootTest
@Transactional
class BoardApiControllerTest {
    @Autowired private MockMvc mvc;
    @Autowired private ObjectMapper mapper;
    @Autowired private BoardRepository boardRepository;
    @InjectMocks private BoardApiController boardApiController;


    Long beforeSave(){
        Board board = Board.builder().content("testcontent").password("123pwd#").build();
        boardRepository.save(board);
        return board.getId();
    }

    @Test
    @DisplayName("작성 성공")
    void save() throws Exception {
        //given
        BoardDTO.InsUpd boardDTO = new BoardDTO.InsUpd();
        boardDTO.setPassword("pwd1234@");
        boardDTO.setContent("내용");
        String content = mapper.writeValueAsString(boardDTO);

        //when //then
        mvc.perform(
                post("/api/board")
                .content(content)
                .contentType(MediaType.APPLICATION_JSON)
        )
        .andExpect(status().isOk())
        .andExpect(jsonPath("$").isNotEmpty())
        .andDo(print());
    }


    @Test
    @DisplayName("작성 실패 - 빈칸 ")
    void saveBlankFail() throws Exception {
        //given
        BoardDTO.InsUpd boardDTO = new BoardDTO.InsUpd();
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
        .andExpect(jsonPath("$[?(@.field=='password' && @.code=='NotBlank')]defaultMessage").value("비밀번호를 입력해주세요."))
        .andExpect(jsonPath("$[?(@.field=='password' && @.code=='Pattern')]defaultMessage").value("비밀번호는 영소문자, 숫자, 특수문자가 1문자 이상 포함되어야하며 5자 이상 20자 이하로 설정해주세요"))
        .andExpect(jsonPath("$[?(@.field=='content')]defaultMessage").value("내용을 입력해주세요."))
        .andDo(print());
    }

    @Test
    @DisplayName("작성 실패 - 내용 100자 이상")
    void saveMaxSizeContentFail() throws Exception {
        //given
        BoardDTO.InsUpd boardDTO = new BoardDTO.InsUpd();
        boardDTO.setPassword("pwd1313@");
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


    @Test
    @DisplayName("수정 실패 - 비밀번호 불일치")
    void updateFail_PasswordException() throws Exception {
        //given
        Long boardId = beforeSave();
        BoardDTO.InsUpd boardDTO = new BoardDTO.InsUpd();
        boardDTO.setPassword("123pwd#@@");
        boardDTO.setContent("updateContent");
        String content = mapper.writeValueAsString(boardDTO);

        //when //then
        mvc.perform(
                put("/api/board/"+boardId)
                        .content(content)
                        .contentType(MediaType.APPLICATION_JSON)
        )
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$[?(@.field=='password')]defaultMessage").value("패스워드가 일치하지 않습니다."))
        .andDo(print());
    }


    @Test
    @DisplayName("수정 성공")
    void updateSuccess() throws Exception {
        //given
        Long boardId = beforeSave();
        BoardDTO.InsUpd boardDTO = new BoardDTO.InsUpd();
        boardDTO.setPassword("123pwd#");
        boardDTO.setContent("updateContent");
        String content = mapper.writeValueAsString(boardDTO);

        //when //then
        mvc.perform(
                put("/api/board/"+boardId)
                        .content(content)
                        .contentType(MediaType.APPLICATION_JSON)
        )
        .andExpect(status().isOk())
        .andDo(print());
    }


    @Test
    @DisplayName("삭제 성공")
    void deleteSuccess() throws Exception {
        //given
        Long boardId = beforeSave();
        BoardDTO.Delete boardDTO = new BoardDTO.Delete();
        boardDTO.setPassword("123pwd#");
        String content = mapper.writeValueAsString(boardDTO);

        //when //then
        mvc.perform(
                delete("/api/board/"+boardId)
                        .content(content)
                        .contentType(MediaType.APPLICATION_JSON)
        )
        .andExpect(status().isOk())
        .andDo(print());
    }
}