package com.example.twitterclone.service;

import com.example.twitterclone.dto.CommentDTO;
import com.example.twitterclone.entity.Board;
import com.example.twitterclone.entity.Comment;
import com.example.twitterclone.repository.BoardRepository;
import com.example.twitterclone.repository.CommentRepository;
import com.example.twitterclone.util.exception.DataBaseException;
import com.example.twitterclone.util.exception.PasswordNotEqualException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@Transactional

@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
class CommentServiceTest {
    @Autowired private BoardRepository boardRepository;
    @Autowired private CommentRepository commentRepository;
    @Autowired private CommentService commentService;

    @Autowired private MockMvc mvc;
    @Autowired private ObjectMapper mapper;

    Board board;
    Comment comment;
    @BeforeEach
    void beforeAll() {
        board = Board.builder()
                .nickname("11")
                .password("!a1234")
                .content("hello")
                .build();
        boardRepository.save(board);

        comment = Comment.builder()
                .board(board)
                .nickname("12")
                .password("a@$1234")
                .content("this is comment")
                .build();

        board.getComments().add(comment);
        commentRepository.save(comment);
    }

    @Test
    @DisplayName("?????? ?????? ??????")
    void fieldValidate() throws Exception {
        CommentDTO.Insert commentAllBlank = new CommentDTO.Insert();
        commentAllBlank.setNickname("");
        commentAllBlank.setPassword("");
        commentAllBlank.setContent("");
        String content = mapper.writeValueAsString(commentAllBlank);

        mvc.perform(
                post("/api/comment/"+board.getId())
                .content(content)
                .contentType(MediaType.APPLICATION_JSON)
        )
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$[?(@.field=='nickname')]defaultMessage").value("???????????? ??????????????????."))
        .andExpect(jsonPath("$[?(@.field=='password' && @.code=='NotBlank')]defaultMessage").value("??????????????? ??????????????????."))
        .andExpect(jsonPath("$[?(@.field=='password' && @.code=='Pattern')]defaultMessage").value("??????????????? ????????????, ??????, ??????????????? 1?????? ?????? ????????????????????? 5??? ?????? 20??? ????????? ??????????????????."))
        .andExpect(jsonPath("$[?(@.field=='content')]defaultMessage").value("????????? ??????????????????."))
        .andDo(print());
    }

    @Test
    @DisplayName("?????? ?????? ??????")
    void saveSuccess() {
        CommentDTO.Insert commentDTO = new CommentDTO.Insert();
        commentDTO.setNickname("11");
        commentDTO.setPassword("a@1234");
        commentDTO.setContent("11");

        Long saveId = commentService.saveComment(board.getId(), commentDTO);
        Assertions.assertThat(saveId).isNotNull();

        List<CommentDTO.Response> comments = commentService.getComments(board.getId());
        Assertions.assertThat(comments.size()).isEqualTo(2);
        Assertions.assertThat(comments.get(1).getContent()).isEqualTo(commentDTO.getContent());
    }

    @Test
    @DisplayName("?????? ?????? ?????? - ????????? ???????????? ??????")
    void saveFail() {
        CommentDTO.Insert commentDTO = new CommentDTO.Insert();
        commentDTO.setNickname("11");
        commentDTO.setPassword("a@$1234");
        commentDTO.setContent("11");

        org.junit.jupiter.api.Assertions.assertThrows(DataBaseException.class, () ->{
            commentService.saveComment(0L, commentDTO);
        });
    }

    @Test
    @DisplayName("?????? ?????? ??????")
    void updateSuccess() {
        CommentDTO.Update commentDTO = new CommentDTO.Update();
        commentDTO.setPassword("a@$1234");
        commentDTO.setContent("11");

        commentService.updateComment(comment.getId(), commentDTO);
    }

    @Test
    @DisplayName("?????? ?????? ?????? - ???????????? ?????????")
    void updateFail() {
        CommentDTO.Update commentDTO = new CommentDTO.Update();
        commentDTO.setPassword("a@1234");
        commentDTO.setContent("11");

        org.junit.jupiter.api.Assertions.assertThrows(PasswordNotEqualException.class, () -> {
            commentService.updateComment(comment.getId(), commentDTO);
        });
    }

    @Test
    @DisplayName("?????? ?????? ??????")
    void deleteSuccess() {
        CommentDTO.Delete commentDTO = new CommentDTO.Delete();
        commentDTO.setPassword("a@$1234");

        commentService.deleteComment(comment.getId(), commentDTO);
    }

}