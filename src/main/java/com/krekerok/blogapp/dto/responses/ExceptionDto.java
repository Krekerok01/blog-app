package com.krekerok.blogapp.dto.responses;

import lombok.Value;

@Value
public class ExceptionDto {

    String message;
    int statusCode;
    String statusMessage;
}
