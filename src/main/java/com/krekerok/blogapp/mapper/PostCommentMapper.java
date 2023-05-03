package com.krekerok.blogapp.mapper;

import com.krekerok.blogapp.dto.responses.CommentResponseDto;
import com.krekerok.blogapp.entity.PostComment;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface PostCommentMapper {

    PostCommentMapper INSTANCE = Mappers.getMapper(PostCommentMapper.class);

    CommentResponseDto toCommentResponseDto(PostComment postComment);
}
