package com.krekerok.blogapp.dto.request;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import lombok.Value;

@Value
public class PostUpdateRequestDto {

    @NotBlank
    @Size(max = 60)
    String header;

    @NotBlank
    @Size(max = 2500)
    String text;
}
