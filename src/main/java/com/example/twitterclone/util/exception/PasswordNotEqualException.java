package com.example.twitterclone.util.exception;

import com.example.twitterclone.dto.BoardDTO;
import com.example.twitterclone.util.ErrorResponse;

public class PasswordNotEqualException extends RuntimeException {

    public PasswordNotEqualException() {
    }

    public PasswordNotEqualException(String message) {
        super(message);
    }
}
