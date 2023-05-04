package com.krekerok.blogapp.dto.request;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import lombok.Value;

@Value
public class CommentRequestDto {

    @NotBlank
    @Size(max = 150, message = "Message max size is 150 symbols")
    String message;

    @JsonCreator
    public CommentRequestDto(@JsonProperty("message") String message) {
        this.message = message;
    }
}