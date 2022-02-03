package com.example.twitterclone.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
@NoArgsConstructor
public class BoardDTO {
    @NotBlank(message = "비밀번호를 입력해주세요.")
    private String password;

    @NotBlank(message = "내용을 입력해주세요.")
    @Size(max = 100, message = "100자 이하로 입력해주세요.")
    private String content;
}
