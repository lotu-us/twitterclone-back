package com.example.twitterclone.util;

import lombok.Data;

@Data
public class ErrorResponse {
    private String field;
    private String objectName;
    private String code;
    private String defaultMessage;
}
