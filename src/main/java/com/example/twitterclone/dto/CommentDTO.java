package com.example.twitterclone.dto;

import com.example.twitterclone.config.PasswordRegexpConfig;
import com.example.twitterclone.entity.Comment;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.modelmapper.ModelMapper;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.util.List;
import java.util.stream.Collectors;


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


    @Data
    @NoArgsConstructor
    public static class Response{
        private Long id;
        private String nickname;
        private String content;

        public Response(Comment comment) {
            this.id = comment.getId();
            this.nickname = comment.getNickname();
            this.content = comment.getContent();
        }
    }


    public static List<Response> convertList(List<Comment> comments) {
        ModelMapper modelMapper = new ModelMapper();

        List<Response> collect =
                comments.stream().map(response ->
                        modelMapper.map(response, Response.class)
                ).collect(Collectors.toList());
        return collect;
    }
}
