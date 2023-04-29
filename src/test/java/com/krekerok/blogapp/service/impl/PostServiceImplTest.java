package com.krekerok.blogapp.service.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import com.krekerok.blogapp.dto.responses.PostResponseDto;
import com.krekerok.blogapp.entity.Post;
import com.krekerok.blogapp.repository.PostRepository;
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

}