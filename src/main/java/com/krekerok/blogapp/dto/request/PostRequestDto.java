package com.krekerok.blogapp.dto.request;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import lombok.Value;
import org.springframework.web.multipart.MultipartFile;

@Value
public class PostRequestDto {

    @NotNull
    MultipartFile imageFile;

    @NotBlank
    @Size(max = 60)
    String header;

    @NotBlank
    @Size(max = 2500)
    String text;

}
