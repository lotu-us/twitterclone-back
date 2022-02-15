package com.example.twitterclone.entity;

import lombok.*;
import org.springframework.util.Assert;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name="board")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Board {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "board_id")
    //@NotNull          //객체 생성 시 null로 잡혀서 오류발생
    private Long id;

    @Column
    @NotNull
    private String nickname;

    @Column
    @NotNull
    private String password;

    @Column
    @NotNull
    private String content;

    @OneToMany(mappedBy = "board", fetch = FetchType.LAZY)
    private List<Comment> comments = new ArrayList<>();


    @Builder
    public Board(String nickname, String password, String content) {
        Assert.notNull(nickname, "글 입력 시 nickname은 null일 수 없습니다.");
        Assert.notNull(password, "글 입력 시 password는 null일 수 없습니다.");
        Assert.notNull(content, "글 입력 시 content null일 수 없습니다.");
        this.nickname = nickname;
        this.password = password;
        this.content = content;
    }


    public void changeContent(String content){
        this.content = content;
    }
}