package com.krekerok.blogapp.service.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrowsExactly;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import com.krekerok.blogapp.dto.requests.PostUpdateRequestDto;
import com.krekerok.blogapp.dto.responses.PostResponseDto;
import com.krekerok.blogapp.entity.Blog;
import com.krekerok.blogapp.entity.Post;
import com.krekerok.blogapp.exception.NoPostIdMatchException;
import com.krekerok.blogapp.mapper.PostMapper;
import com.krekerok.blogapp.repository.PostRepository;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
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
    @Mock
    private CloudinaryServiceImpl cloudinaryService;

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

        Post post = buildPost();

        PostUpdateRequestDto postUpdateRequestDto = new PostUpdateRequestDto("updated header", "updated text");

        doReturn(Optional.of(post)).when(postRepository).findById(1L);
        doReturn(true).when(appUserService).checkingForDataCompliance(1L, jwt);
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

        Post post = buildPost();

        doReturn(Optional.of(post)).when(postRepository).findById(1L);
        doReturn(false).when(appUserService).checkingForDataCompliance(1L, jwt);

        Exception exception = assertThrowsExactly(NoPostIdMatchException.class,
            () -> postService.updatePostTextInfo(1L, any(PostUpdateRequestDto.class), jwt));

        String expectedMessage = "Invalid post id";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));

        verifyNoMoreInteractions(postRepository);
    }

    @Test
    void testDeletePost(){
        Post post = buildPost();

        doReturn(Optional.of(post)).when(postRepository).findById(1L);

        postService.deletePost(1L);

        verify(cloudinaryService, times(1)).deleteFile(post.getImageURL());
        verify(postRepository, times(1)).delete(post);
        verifyNoMoreInteractions(postRepository);
    }

    @Test
    void testGetAllPostsByBlog(){
        Blog blog = Blog.builder().blogId(1L).build();

        List<Post> posts = List.of(buildPost(), buildPost(), buildPost());
        doReturn(posts).when(postRepository).findAllByBlog(blog);

        List<PostResponseDto> result = postService.getAllPostsByBlog(blog);

        assertNotNull(result);
        assertEquals(posts.size(), result.size());
        verify(postRepository, times(1)).findAllByBlog(blog);
        verifyNoMoreInteractions(postRepository);
    }

    private Post buildPost(){
        return Post.builder()
            .postId(1L)
            .blog(Blog.builder().blogId(1L).build())
            .header("old header")
            .text("old text")
            .imageURL("image url")
            .build();
    }
}