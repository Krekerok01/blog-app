package com.krekerok.blogapp.service.impl;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import com.krekerok.blogapp.entity.Blog;
import com.krekerok.blogapp.exception.BlogNotFoundException;
import com.krekerok.blogapp.repository.BlogRepository;
import java.time.Instant;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class BlogServiceImplTest {

    @Mock
    private BlogRepository blogRepository;

    @InjectMocks
    private BlogServiceImpl blogService;

    @Test
    void testFindBlogById_shouldReturnBlog() {
        Blog blog = Blog.builder()
            .blogId(1L)
            .blogName("Test blog name")
            .createdAt(Instant.now())
            .modifiedAt(Instant.now())
            .build();

        doReturn(Optional.of(blog)).when(blogRepository).findById(anyLong());

        Blog actualResult = blogService.findBlogById(anyLong());

        assertNotNull(actualResult);
        assertEquals(blog, actualResult);
        verify(blogRepository, times(1)).findById(anyLong());
    }

    @Test
    void testFindBlogById_shouldThrowBlogNotFoundException() {

        doReturn(Optional.empty()).when(blogRepository).findById(anyLong());

        Exception exception = assertThrows(BlogNotFoundException.class, () -> blogService.findBlogById(anyLong()));

        String expectedMessage = "Blog not found";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }
}