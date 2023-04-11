package com.krekerok.blogapp.mapper;

import com.krekerok.blogapp.dto.requests.BlogRequestDto;
import com.krekerok.blogapp.dto.responses.BlogResponseDto;
import com.krekerok.blogapp.entity.Blog;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface BlogMapper {
    BlogMapper INSTANCE = Mappers.getMapper(BlogMapper.class);

    BlogResponseDto toBlogResponseDto(Blog blog);

    @Mapping(target = "blogId", ignore = true)
    @Mapping(target = "createdAt", expression = "java(java.time.Instant.now())")
    @Mapping(target = "modifiedAt", expression = "java(java.time.Instant.now())")
    Blog toBlog(BlogRequestDto blogRequestDto);
}
