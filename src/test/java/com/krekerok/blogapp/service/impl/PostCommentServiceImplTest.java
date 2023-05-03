package com.krekerok.blogapp.service.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;

import com.krekerok.blogapp.dto.requests.CommentRequestDto;
import com.krekerok.blogapp.dto.responses.CommentResponseDto;
import com.krekerok.blogapp.entity.AppUser;
import com.krekerok.blogapp.entity.Post;
import com.krekerok.blogapp.entity.PostComment;
import com.krekerok.blogapp.repository.PostCommentRepository;
import com.krekerok.blogapp.service.AppUserService;
import com.krekerok.blogapp.service.PostService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class PostCommentServiceImplTest {

    @Mock
    private AppUserService appUserService;
    @Mock
    private PostService postService;
    @Mock
    private PostCommentRepository postCommentRepository;
    @InjectMocks
    private PostCommentServiceImpl postCommentService;

    @Test
    void testCreateComment() {

        AppUser appUser = AppUser.builder().username("username").userId(1L).build();
        Post post = Post.builder().postId(1L).build();
        PostComment postComment = buildPostComment(appUser, post);

        doReturn(appUser).when(appUserService).findAppUserByUsernameFromJWT(anyString());
        doReturn(post).when(postService).findPostByPostId(anyLong());
        doReturn(postComment).when(postCommentRepository).save(postComment);

        CommentResponseDto result = postCommentService
            .createComment(1L, new CommentRequestDto("test message"), anyString());

        assertNotNull(result);
        assertEquals(postComment.getPostId(), result.getPostId());
        assertEquals(postComment.getAuthorUsername(), result.getAuthorUsername());
        assertEquals(postComment.getMessage(), result.getMessage());
        verify(postCommentRepository, times(1)).save(postComment);
        verifyNoMoreInteractions(postCommentRepository);
    }

    private PostComment buildPostComment(AppUser appUser, Post post) {
        return PostComment.builder()
            .appUserId(appUser.getUserId())
            .authorUsername(appUser.getUsername())
            .postId(post.getPostId())
            .message("test message")
            .build();
    }
}