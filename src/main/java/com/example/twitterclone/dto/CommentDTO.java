package com.example.twitterclone.dto;

import com.example.twitterclone.config.PasswordRegexpConfig;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;


public class CommentDTO {

    @Data
    @NoArgsConstructor
    public static class Insert {
        @NotBlank(message = "{nickname.NotBlank}")
        @Size(max = 10, message = "{nickname.SizeMax}")
        private String nickname;

        @NotBlank(message = "{password.NotBlank}")
        @Pattern(regexp = PasswordRegexpConfig.reg, message = "{password.Pattern}")
        private String password;

        @NotBlank(message = "{content.NotBlank}")
        @Size(max = 50, message = "{content.SizeMax}")
        private String content;
    }

    @Data
    @NoArgsConstructor
    public static class Update{
        @NotBlank(message = "{password.NotBlank}")
        @Pattern(regexp = PasswordRegexpConfig.reg, message = "{password.Pattern}")
        private String password;

        @NotBlank(message = "{content.NotBlank}")
        @Size(max = 50, message = "{content.SizeMax}")
        private String content;
    }

    @Data
    @NoArgsConstructor
    public static class Delete{
        @NotBlank(message = "{password.NotBlank}")
        @Pattern(regexp = PasswordRegexpConfig.reg, message = "{password.Pattern}")
        private String password;
    }

}
