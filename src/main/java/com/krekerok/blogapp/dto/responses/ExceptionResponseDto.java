package com.krekerok.blogapp.dto.responses;

import lombok.Value;

@Value
public class ExceptionResponseDto {

    String message;
    int statusCode;
    String statusMessage;
}
