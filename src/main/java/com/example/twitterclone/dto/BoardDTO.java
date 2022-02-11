package com.example.twitterclone.dto;

import com.example.twitterclone.config.PasswordRegexpConfig;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;


public class BoardDTO {

    @Data
    @NoArgsConstructor
    public static class InsUpd{     //삽입 및 수정 시 사용
        @NotBlank(message = "비밀번호를 입력해주세요.")
        @Pattern(regexp = PasswordRegexpConfig.reg, message = "비밀번호는 영소문자, 숫자, 특수문자가 1문자 이상 포함되어야하며 5자 이상 20자 이하로 설정해주세요")
        private String password;

        @NotBlank(message = "내용을 입력해주세요.")
        @Size(max = 100, message = "100자 이하로 입력해주세요.")
        private String content;
    }

    @Data
    @NoArgsConstructor
    public static class Delete{
        @NotBlank(message = "비밀번호를 입력해주세요.")
        @Pattern(regexp = PasswordRegexpConfig.reg, message = "비밀번호는 영소문자, 숫자, 특수문자가 1문자 이상 포함되어야하며 5자 이상 20자 이하로 설정해주세요")
        private String password;
    }

}
