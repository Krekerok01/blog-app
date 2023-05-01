package com.krekerok.blogapp.service.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrowsExactly;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import com.krekerok.blogapp.dto.requests.PostUpdateRequestDto;
import com.krekerok.blogapp.dto.responses.PostResponseDto;
import com.krekerok.blogapp.entity.Blog;
import com.krekerok.blogapp.entity.Post;
import com.krekerok.blogapp.exception.BlogNotFoundException;
import com.krekerok.blogapp.exception.NoBlogIdMatchException;
import com.krekerok.blogapp.exception.NoPostIdMatchException;
import com.krekerok.blogapp.mapper.PostMapper;
import com.krekerok.blogapp.repository.PostRepository;
import java.time.Instant;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class PostServiceImplTest {

    @Mock
    private PostRepository postRepository;
    @Mock
    private AppUserServiceImpl appUserService;

    @InjectMocks
    private PostServiceImpl postService;

    @Test
    void testGetPost(){
        Post post = Post.builder().postId(1L).build();
        PostResponseDto postResponseDto = PostResponseDto.builder().postId(1L).build();


        when(postRepository.findById(1L)).thenReturn(Optional.of(post));

        PostResponseDto result = postService.getPost(1L);

        assertNotNull(result);
        assertEquals(postResponseDto, result);
        verify(postRepository, times(1)).findById(1L);
    }

    @Test
    void existsByPostId_ReturnTrue(){
        boolean expected = true;
        doReturn(expected).when(postRepository).existsById(anyLong());

        boolean result = postService.existsByPostId(anyLong());

        assertTrue(result);
        assertEquals(expected, result);
        verify(postRepository, times(1)).existsById(anyLong());
        verifyNoMoreInteractions(postRepository);
    }

    @Test
    void testUpdatePostTextInfo_ShouldUpdatePost(){
        String jwt = "valid_jwt";
        Blog blog = buildBlog();
        Post post = buildPost(blog);

        PostUpdateRequestDto postUpdateRequestDto = new PostUpdateRequestDto("updated header", "updated text");

        doReturn(Optional.of(post)).when(postRepository).findById(1L);
        doReturn(true).when(appUserService).checkingForDataCompliance(blog.getBlogId(), jwt);
        doReturn(post).when(postRepository).save(post);

        PostResponseDto responseDto = postService.updatePostTextInfo(1L, postUpdateRequestDto, jwt);

        assertEquals(postUpdateRequestDto.getHeader(), post.getHeader());
        assertEquals(postUpdateRequestDto.getText(), post.getText());

        assertEquals(postUpdateRequestDto.getHeader(), responseDto.getHeader());
        assertEquals(postUpdateRequestDto.getText(), responseDto.getText());
        verifyNoMoreInteractions(postRepository);

    }

    @Test
    void testUpdatePostTextInfo_ShouldThrowException(){
        String jwt = "valid_jwt";
        Blog blog = buildBlog();
        Post post = buildPost(blog);

        doReturn(Optional.of(post)).when(postRepository).findById(1L);
        doReturn(false).when(appUserService).checkingForDataCompliance(blog.getBlogId(), jwt);

        Exception exception = assertThrowsExactly(NoPostIdMatchException.class,
            () -> postService.updatePostTextInfo(1L, any(PostUpdateRequestDto.class), jwt));

        String expectedMessage = "Invalid post id";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));

        verifyNoMoreInteractions(postRepository);
    }

    private Blog buildBlog() {
        return Blog.builder().blogId(1L).build();
    }

    private Post buildPost(Blog blog){
        return Post.builder()
            .postId(1L)
            .blog(blog)
            .header("old header")
            .text("old text")
            .build();
    }
}