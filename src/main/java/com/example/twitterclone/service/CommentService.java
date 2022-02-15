package com.example.twitterclone.service;

import com.example.twitterclone.dto.CommentDTO;
import com.example.twitterclone.entity.Board;
import com.example.twitterclone.entity.Comment;
import com.example.twitterclone.repository.BoardRepository;
import com.example.twitterclone.repository.CommentRepository;
import com.example.twitterclone.util.exception.DataBaseException;
import com.example.twitterclone.util.exception.PasswordNotEqualException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class CommentService {
    private final BoardRepository boardRepository;
    private final CommentRepository commentRepository;

    public Long saveComment(Long boardId, CommentDTO.Insert commentDTO) {
        Board board = boardRepository.findById(boardId).orElse(null);
        if(board == null){
            throw new DataBaseException("게시글이 존재하지 않습니다.");
        }

        Comment comment = Comment.builder()
                .board(board)
                .nickname(commentDTO.getNickname())
                .password(commentDTO.getPassword())
                .content(commentDTO.getContent())
                .build();

        //연관관계 설정 :
        // 주인인 곳에만 연관관계를 설정하면 board.getComments 하면 추가되어있지 않다.
        // 주인이 아닌 곳에만 연관관계를 설정하면 comment의 board_id가 null로 입력된다.
        // 결론 : 주인, 주인 아닌곳 둘다 설정해줄 것
        board.getComments().add(comment);

        commentRepository.save(comment);
        if(comment.getId() == null){
            throw new DataBaseException("댓글이 저장되지 않았습니다.");
        }
        return comment.getId();
    }


    public List<Comment> getComments(Long boardId) {
        Board board = boardRepository.findById(boardId).orElse(null);
        if(board == null){
            throw new DataBaseException("게시글이 존재하지 않습니다.");
        }
        List<Comment> comments = board.getComments();
        return comments;
    }

    public void updateComment(Long commentId, CommentDTO.Update commentDTO) {
        Comment comment = commentRepository.findById(commentId).orElse(null);
        if( !comment.getPassword().equals(commentDTO.getPassword()) ){
            throw new PasswordNotEqualException();
        }

        comment.changeContent(commentDTO.getContent());
        commentRepository.save(comment);
    }

    public void deleteComment(Long commentId, CommentDTO.Delete commentDTO) {
        Comment comment = commentRepository.findById(commentId).orElse(null);
        if( !comment.getPassword().equals(commentDTO.getPassword()) ){
            throw new PasswordNotEqualException();
        }

        commentRepository.deleteById(comment.getId());
    }
}
