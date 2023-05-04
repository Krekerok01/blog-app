package com.krekerok.blogapp.service.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
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
import com.krekerok.blogapp.exception.sucurity.ForbiddingException;
import com.krekerok.blogapp.repository.PostRepository;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

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

        Exception exception = assertThrowsExactly(ForbiddingException.class,
            () -> postService.updatePostTextInfo(1L, any(PostUpdateRequestDto.class), jwt));

        String expectedMessage = "The user can update only his post.";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));

        verifyNoMoreInteractions(postRepository);
    }

    @Test
    void testUpdatePostImage_ShouldUpdatePost(){
        String jwt = "valid_jwt";
        Post post = buildPost();
        String url = "new image url";

        MultipartFile file = new MockMultipartFile("test.png", "test picture".getBytes());

        doReturn(Optional.of(post)).when(postRepository).findById(1L);
        doReturn(true).when(appUserService).checkingForDataCompliance(1L, jwt);
        doReturn(url).when(cloudinaryService).uploadFile(file);
        doReturn(post).when(postRepository).save(post);

        PostResponseDto responseDto = postService.updatePostImage(1L, file, jwt);

        assertNotNull(responseDto);
        assertEquals(responseDto.getImageURL(), url);
        assertEquals(responseDto.getImageURL(), post.getImageURL());
        assertEquals(responseDto.getPostId(), post.getPostId());

        verify(postRepository, times(1)).findById(1L);
        verify(cloudinaryService, times(1)).uploadFile(file);
        verify(postRepository, times(1)).save(post);
        verifyNoMoreInteractions(postRepository);
    }


    @Test
    void testUpdatePostImage_ShouldThrowException(){
        String jwt = "valid_jwt";
        Post post = buildPost();

        doReturn(Optional.of(post)).when(postRepository).findById(1L);
        doReturn(false).when(appUserService).checkingForDataCompliance(1L, jwt);

        Exception exception = assertThrowsExactly(ForbiddingException.class,
            () -> postService.updatePostImage(1L, any(MultipartFile.class), jwt));

        String expectedMessage = "The user can update only his post.";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));

        verify(postRepository, times(1)).findById(1L);
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
    void testGetAllPostsByBlog_ShouldReturnFullList(){
        Blog blog = Blog.builder().blogId(1L).build();

        List<Post> posts = List.of(buildPost(), buildPost(), buildPost());
        doReturn(posts).when(postRepository).findAllByBlog(blog);

        List<PostResponseDto> result = postService.getAllPostsByBlog(blog);

        assertNotNull(result);
        assertEquals(posts.size(), result.size());
        verify(postRepository, times(1)).findAllByBlog(blog);
        verifyNoMoreInteractions(postRepository);
    }

    @Test
    void testGetAllPostsByBlog_ShouldReturnNull(){
        Blog blog = Blog.builder().blogId(1L).build();
        List<Post> posts = new ArrayList<>();

        doReturn(posts).when(postRepository).findAllByBlog(blog);

        List<PostResponseDto> result = postService.getAllPostsByBlog(blog);

        assertNull(result);
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