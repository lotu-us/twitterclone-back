package com.example.twitterclone.service;

import com.example.twitterclone.dto.BoardDTO;
import com.example.twitterclone.entity.Board;
import com.example.twitterclone.repository.BoardRepository;
import com.example.twitterclone.util.exception.DataBaseException;
import com.example.twitterclone.util.exception.PasswordNotEqualException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class BoardService {
    private final BoardRepository boardRepository;

    public Long saveBoard(BoardDTO.Insert boardDTO){
        Board board = Board.builder()
                .nickname(boardDTO.getNickname())
                .password(boardDTO.getPassword())
                .content(boardDTO.getContent())
                .build();
        boardRepository.save(board);
        if(board.getId() == null){
            throw new DataBaseException("게시글이 저장되지 않았습니다.");
        }
        return board.getId();
    }

    public List<BoardDTO.Response> getBoards() {
        List<BoardDTO.Response> boards = BoardDTO.convertList(boardRepository.findAll());
        return boards;
    }

    public void updateBoard(Long boardId, BoardDTO.Update boardDTO){
        Board board = boardRepository.findById(boardId).orElse(null);
        if(! board.getPassword().equals(boardDTO.getPassword())){
            throw new PasswordNotEqualException();
        }

        board.changeContent(boardDTO.getContent());
        boardRepository.save(board);
    }


    public void deleteBoard(Long boardId, BoardDTO.Delete boardDTO) {
        Board board = boardRepository.findById(boardId).orElse(null);
        if(! board.getPassword().equals(boardDTO.getPassword())){
            throw new PasswordNotEqualException();
        }

        boardRepository.deleteById(board.getId());
    }

}
