package com.krekerok.blogapp.service.impl;

import com.krekerok.blogapp.dto.requests.CommentRequestDto;
import com.krekerok.blogapp.dto.responses.CommentResponseDto;
import com.krekerok.blogapp.entity.AppUser;
import com.krekerok.blogapp.entity.Post;
import com.krekerok.blogapp.entity.PostComment;
import com.krekerok.blogapp.exception.ForbiddingException;
import com.krekerok.blogapp.exception.PostCommentNotFoundException;
import com.krekerok.blogapp.mapper.PostCommentMapper;
import com.krekerok.blogapp.repository.PostCommentRepository;
import com.krekerok.blogapp.service.AppUserService;
import com.krekerok.blogapp.service.PostCommentService;
import com.krekerok.blogapp.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PostCommentServiceImpl implements PostCommentService {

    @Autowired
    private PostCommentRepository postCommentRepository;
    @Autowired
    private PostService postService;
    @Autowired
    private AppUserService appUserService;

    @Override
    public CommentResponseDto createComment(Long postId, CommentRequestDto commentRequestDto, String jwt) {

        Post post = postService.findPostByPostId(postId);
        AppUser appUser = appUserService.findAppUserByUsernameFromJWT(jwt);


        PostComment savedComment = postCommentRepository.save(PostComment.builder()
            .appUserId(appUser.getUserId())
            .postId(post.getPostId())
            .authorUsername(appUser.getUsername())
            .message(commentRequestDto.getMessage())
            .build());

        return PostCommentMapper.INSTANCE.toCommentResponseDto(savedComment);
    }

    @Override
    public void deleteComment(Long commentId, String jwt) {
        PostComment postComment = findPostCommentByCommentId(commentId);
        AppUser appUser = appUserService.findAppUserByUsernameFromJWT(jwt);
        if (postComment.getAppUserId().equals(appUser.getUserId())){
            postCommentRepository.delete(postComment);
        } else {
            throw new ForbiddingException("The user can delete only his comments.");
        }
    }

    @Override
    public PostComment findPostCommentByCommentId(Long commentId) {
        return postCommentRepository.findById(commentId)
            .orElseThrow(() -> new PostCommentNotFoundException("Comment not found."));
    }
}
