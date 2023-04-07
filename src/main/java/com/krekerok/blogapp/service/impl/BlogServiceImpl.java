package com.krekerok.blogapp.service.impl;

import com.krekerok.blogapp.dto.requests.BlogCreateDto;
import com.krekerok.blogapp.dto.responses.BlogReadDto;
import com.krekerok.blogapp.entity.AppUser;
import com.krekerok.blogapp.entity.Blog;
import com.krekerok.blogapp.exception.BlogExistsException;
import com.krekerok.blogapp.exception.BlogNotFoundException;
import com.krekerok.blogapp.exception.NoBlogIdMatchException;
import com.krekerok.blogapp.repository.BlogRepository;
import com.krekerok.blogapp.service.AppUserService;
import com.krekerok.blogapp.service.BlogService;
import java.time.Instant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class BlogServiceImpl implements BlogService {

    @Autowired
    private AppUserService appUserService;
    @Autowired
    private BlogRepository blogRepository;

    @Override
    public BlogReadDto createBlog(Long appUserId, BlogCreateDto blogCreateDto) {
        AppUser appUser = appUserService.findAppUserByAppUserId(appUserId);

        if (appUser.getBlog() == null) {
            Blog blog = Blog.builder().blogName(blogCreateDto.getBlogName())
                .createdAt(Instant.now()).modifiedAt(Instant.now()).build();
            appUser.setBlog(blog);
            AppUser appUser1 = appUserService.saveBlogToTheAppUser(appUser);
            return BlogReadDto.builder()
                .blogId(appUser1.getBlog().getBlogId())
                .blogName(blogCreateDto.getBlogName())
                .createdAt(appUser1.getBlog().getCreatedAt())
                .build();
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
            throw new NoBlogIdMatchException("Invalid blog id");
        }
    }
}
