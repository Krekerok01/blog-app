package com.krekerok.blogapp.dto.requests;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import lombok.Value;

@Value
public class BlogRequestDto {

    @NotBlank
    @Size(min = 5, max = 40, message = "Blog name min size is 5 symbols and max size is 40 symbols")
    @Pattern(regexp = "[a-zA-Z ]{5,40}", message = "Blog name can contains only letters and spaces.")
    String blogName;

    @JsonCreator
    public BlogRequestDto(@JsonProperty("blogName") String blogName) {
        this.blogName = blogName;
    }
}
