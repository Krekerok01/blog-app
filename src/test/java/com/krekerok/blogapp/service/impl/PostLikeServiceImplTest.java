package com.krekerok.blogapp.service.impl;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;

import com.krekerok.blogapp.entity.AppUser;
import com.krekerok.blogapp.entity.Post;
import com.krekerok.blogapp.entity.PostLike;
import com.krekerok.blogapp.repository.PostLikeRepository;
import com.krekerok.blogapp.service.AppUserService;
import com.krekerok.blogapp.service.PostService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class PostLikeServiceImplTest {

    @Mock
    private AppUserService appUserService;
    @Mock
    private PostService postService;
    @Mock
    private PostLikeRepository postLikeRepository;
    @InjectMocks
    private PostLikeServiceImpl postLikeService;

    @Test
    void testPutLikeOrRemoveItFromThePost_ShouldReturnTrue() {

        AppUser appUser = AppUser.builder().userId(1L).build();
        Post post = Post.builder().postId(1L).build();

        doReturn(appUser).when(appUserService).findAppUserByUsernameFromJWT(anyString());
        doReturn(post).when(postService).findPostByPostId(anyLong());
        doReturn(false).when(postLikeRepository).existsByAppUserIdAndPostId(anyLong(), anyLong());
        boolean result = postLikeService.putLikeOrRemoveItFromThePost(1L, anyString());

        assertTrue(result);
        verify(postLikeRepository, times(1)).save(any(PostLike.class));
        verifyNoMoreInteractions(postLikeRepository);
    }

    @Test
    void testPutLikeOrRemoveItFromThePost_ShouldReturnFalse() {

        AppUser appUser = AppUser.builder().userId(1L).build();
        Post post = Post.builder().postId(1L).build();

        doReturn(appUser).when(appUserService).findAppUserByUsernameFromJWT(anyString());
        doReturn(post).when(postService).findPostByPostId(anyLong());
        doReturn(true).when(postLikeRepository).existsByAppUserIdAndPostId(anyLong(), anyLong());
        boolean result = postLikeService.putLikeOrRemoveItFromThePost(1L, anyString());

        assertFalse(result);
        verify(postLikeRepository, times(1)).deleteByAppUserIdAndPostId(anyLong(), anyLong());
        verifyNoMoreInteractions(postLikeRepository);
    }
}