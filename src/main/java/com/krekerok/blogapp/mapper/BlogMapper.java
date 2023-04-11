package com.krekerok.blogapp.mapper;

import com.krekerok.blogapp.dto.responses.BlogResponseDto;
import com.krekerok.blogapp.entity.Blog;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface BlogMapper {
    BlogMapper INSTANCE = Mappers.getMapper(BlogMapper.class);

    BlogResponseDto toBlogResponseDto(Blog blog);
}
