package com.example.twitterclone.controller;

import com.example.twitterclone.dto.CommentDTO;
import com.example.twitterclone.entity.Comment;
import com.example.twitterclone.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class CommentApiController {
    @Autowired private CommentService commentService;

    @PostMapping("/comment/{boardId}")
    public ResponseEntity saveComment(@PathVariable Long boardId, @Validated @RequestBody CommentDTO.Insert commentDTO, BindingResult bindingResult){
        if(bindingResult.hasErrors()){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(bindingResult);
        }
        Long commentId = commentService.saveComment(boardId, commentDTO);
        return ResponseEntity.status(HttpStatus.OK).body(commentId);
    }

    @GetMapping("/comments/{boardId}")
    public ResponseEntity getComments(@PathVariable Long boardId){
        List<Comment> comments = commentService.getComments(boardId);
        return ResponseEntity.status(HttpStatus.OK).body(comments);
    }

    @PutMapping("/comment/{commentId}")
    public ResponseEntity updateComment(@PathVariable Long commentId, @Validated @RequestBody CommentDTO.Update commentDTO, BindingResult bindingResult){
        if(bindingResult.hasErrors()){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(bindingResult);
        }
        commentService.updateComment(commentId, commentDTO);
        return ResponseEntity.status(HttpStatus.OK).body("ok");
    }

    @DeleteMapping("/comment/{commentId}")
    public ResponseEntity deleteComment(@PathVariable Long commentId, @Validated @RequestBody CommentDTO.Delete commentDTO, BindingResult bindingResult){
        if(bindingResult.hasErrors()){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(bindingResult);
        }
        commentService.deleteComment(commentId, commentDTO);
        return ResponseEntity.status(HttpStatus.OK).body("ok");
    }
}
