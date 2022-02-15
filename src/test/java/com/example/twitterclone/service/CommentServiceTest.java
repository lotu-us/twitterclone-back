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
    @DisplayName("필드 검증 확인")
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
        .andExpect(jsonPath("$[?(@.field=='nickname')]defaultMessage").value("닉네임을 입력해주세요."))
        .andExpect(jsonPath("$[?(@.field=='password' && @.code=='NotBlank')]defaultMessage").value("비밀번호를 입력해주세요."))
        .andExpect(jsonPath("$[?(@.field=='password' && @.code=='Pattern')]defaultMessage").value("비밀번호는 영소문자, 숫자, 특수문자가 1문자 이상 포함되어야하며 5자 이상 20자 이하로 설정해주세요."))
        .andExpect(jsonPath("$[?(@.field=='content')]defaultMessage").value("내용을 입력해주세요."))
        .andDo(print());
    }

    @Test
    @DisplayName("댓글 저장 성공")
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
    @DisplayName("댓글 저장 실패 - 게시글 존재하지 않음")
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
    @DisplayName("댓글 수정 성공")
    void updateSuccess() {
        CommentDTO.Update commentDTO = new CommentDTO.Update();
        commentDTO.setPassword("a@$1234");
        commentDTO.setContent("11");

        commentService.updateComment(comment.getId(), commentDTO);
    }

    @Test
    @DisplayName("댓글 수정 실패 - 비밀번호 불일치")
    void updateFail() {
        CommentDTO.Update commentDTO = new CommentDTO.Update();
        commentDTO.setPassword("a@1234");
        commentDTO.setContent("11");

        org.junit.jupiter.api.Assertions.assertThrows(PasswordNotEqualException.class, () -> {
            commentService.updateComment(comment.getId(), commentDTO);
        });
    }

    @Test
    @DisplayName("댓글 삭제 성공")
    void deleteSuccess() {
        CommentDTO.Delete commentDTO = new CommentDTO.Delete();
        commentDTO.setPassword("a@$1234");

        commentService.deleteComment(comment.getId(), commentDTO);
    }

}