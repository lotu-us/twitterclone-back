package com.example.twitterclone.util.exception;

import com.example.twitterclone.dto.BoardDTO;
import com.example.twitterclone.util.ErrorResponse;

public class PasswordNotEqualException extends RuntimeException {

    public PasswordNotEqualException() {
        super("패스워드가 일치하지 않습니다.");
    }

    public PasswordNotEqualException(String message) {
        super(message);
    }
}
