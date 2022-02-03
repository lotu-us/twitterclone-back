package com.example.twitterclone.entity;

import lombok.*;
import org.springframework.util.Assert;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@Table(name="board")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Board {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    //@NotNull          //객체 생성 시 null로 잡혀서 오류발생
    private Long id;

    @Column
    @NotNull
    private String password;

    @Column
    @NotNull
    private String content;

    @Builder
    public Board(String password, String content) {
        Assert.notNull(password, "글 작성 시 password는 null일 수 없습니다.");
        Assert.notNull(content, "글 작성 시 content null일 수 없습니다.");
        this.password = password;
        this.content = content;
    }
}