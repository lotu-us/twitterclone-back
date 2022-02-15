package com.example.twitterclone.repository;

import com.example.twitterclone.entity.Board;
import com.example.twitterclone.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {
}
