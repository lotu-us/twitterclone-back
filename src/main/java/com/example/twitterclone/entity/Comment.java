package com.example.twitterclone.entity;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.util.Assert;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "comment")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "comment_id")
    private Long id;

    @Column
    @NotNull
    private String nickname;

    @Column
    @NotNull
    private String content;

    @Column
    @NotNull
    private String password;

    @ManyToOne
    @JoinColumn(name = "board_id")
    private Board board;

    @Builder
    public Comment(Board board, String nickname, String content, String password) {
        Assert.notNull(board, "댓글 입력 시 board은 null일 수 없습니다.");
        Assert.notNull(nickname, "댓글 입력 시 nickname은 null일 수 없습니다.");
        Assert.notNull(password, "댓글 입력 시 password는 null일 수 없습니다.");
        Assert.notNull(content, "댓글 입력 시 content는 null일 수 없습니다.");
        this.board = board;
        this.nickname = nickname;
        this.content = content;
        this.password = password;
    }

    public void changeContent(String content) {
        this.content = content;
    }
}
