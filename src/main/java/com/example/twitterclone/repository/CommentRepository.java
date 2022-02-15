package com.example.twitterclone.repository;

import com.example.twitterclone.entity.Board;
import com.example.twitterclone.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional(readOnly=true)
public interface CommentRepository extends JpaRepository<Comment, Long> {
}
