package com.krekerok.blogapp.service.impl;

import com.krekerok.blogapp.dto.request.BlogRequestDto;
import com.krekerok.blogapp.dto.response.BlogAndPostsResponseDto;
import com.krekerok.blogapp.dto.response.BlogResponseDto;
import com.krekerok.blogapp.dto.response.PostResponseDto;
import com.krekerok.blogapp.entity.AppUser;
import com.krekerok.blogapp.entity.Blog;
import com.krekerok.blogapp.exception.data.BlogExistsException;
import com.krekerok.blogapp.exception.data.BlogNotFoundException;
import com.krekerok.blogapp.exception.sucurity.ForbiddingException;
import com.krekerok.blogapp.mapper.BlogMapper;
import com.krekerok.blogapp.repository.BlogRepository;
import com.krekerok.blogapp.service.AppUserService;
import com.krekerok.blogapp.service.BlogService;
import com.krekerok.blogapp.service.PostService;
import java.time.Instant;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class BlogServiceImpl implements BlogService {

    @Autowired
    private AppUserService appUserService;
    @Autowired
    private BlogRepository blogRepository;
    @Autowired
    @Lazy
    private PostService postService;

    @Override
    public BlogResponseDto createBlog(Long appUserId, BlogRequestDto blogRequestDto) {
        AppUser appUser = appUserService.findAppUserByAppUserId(appUserId);

        if (appUser.getBlog() == null) {
            Blog blog = BlogMapper.INSTANCE.toBlog(blogRequestDto);
            appUser.setBlog(blog);
            AppUser savedAppUser = appUserService.saveAppUser(appUser);
            Blog savedBlog = savedAppUser.getBlog();

            return BlogMapper.INSTANCE.toBlogResponseDto(savedBlog);
        } else {
            throw new BlogExistsException("User cannot have more than one blog.");
        }
    }

    @Override
    public Blog findBlogById(Long blogId) {
        return blogRepository.findById(blogId).orElseThrow(() -> new BlogNotFoundException("Blog not found"));
    }

    @Override
    @Transactional
    public void deleteBlogById(long id, String jwt) {
        Blog blog = blogRepository.findById(id).orElseThrow(() -> new BlogNotFoundException("Blog not found"));
        if (appUserService.checkingForDataCompliance(blog.getBlogId(), jwt)){
            appUserService.deleteLinkToTheBlog(blog);
            blogRepository.deleteById(id);
        } else {
            throw new ForbiddingException("The user can delete only his blog.");
        }
    }

    @Override
    public BlogResponseDto updateBlog(long blogId, BlogRequestDto blogRequestDto, String jwt) {
        Blog blog = blogRepository.findById(blogId).orElseThrow(() -> new BlogNotFoundException("Blog not found"));
        if (appUserService.checkingForDataCompliance(blog.getBlogId(), jwt)){
            blog.setBlogName(blogRequestDto.getBlogName());
            blog.setModifiedAt(Instant.now());
            Blog updatedBlog = blogRepository.save(blog);
            return BlogMapper.INSTANCE.toBlogResponseDto(updatedBlog);
        } else {
            throw new ForbiddingException("The user can update only his blog.");
        }
    }

    @Override
    public BlogAndPostsResponseDto getBlogWithPosts(long blogId) {
        Blog blog = blogRepository.findById(blogId).orElseThrow(() -> new BlogNotFoundException("Blog not found"));

        List<PostResponseDto> posts = postService.getAllPostsByBlog(blog);

        return BlogAndPostsResponseDto.builder()
            .blogId(blog.getBlogId())
            .blogName(blog.getBlogName())
            .createdAt(blog.getCreatedAt())
            .modifiedAt(blog.getModifiedAt())
            .posts(posts)
            .build();
    }
}
