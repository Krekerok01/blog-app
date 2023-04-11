package com.krekerok.blogapp.service.impl;

import com.krekerok.blogapp.dto.requests.PostRequestDto;
import com.krekerok.blogapp.entity.Blog;
import com.krekerok.blogapp.entity.Post;
import com.krekerok.blogapp.exception.PostNotFoundException;
import com.krekerok.blogapp.repository.PostRepository;
import com.krekerok.blogapp.service.BlogService;
import com.krekerok.blogapp.service.CloudinaryService;
import com.krekerok.blogapp.service.PostService;
import java.time.Instant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class PostServiceImpl implements PostService {

    @Autowired
    private PostRepository postRepository;
    @Autowired
    private CloudinaryService cloudinaryService;
    @Autowired
    private BlogService blogService;

    @Transactional
    @Override
    public Long createPost(Long blogId, PostRequestDto postRequestDto) {
        Blog blog = blogService.findBlogById(blogId);
        String url =  cloudinaryService.uploadFile(postRequestDto.getImageFile());
        Post post = postRepository.save(getPost(blog, url, postRequestDto));
        return post.getPostId();
    }

    private Post getPost(Blog blog, String url, PostRequestDto postRequestDto){
        return Post.builder()
            .header(postRequestDto.getHeader())
            .text(postRequestDto.getText())
            .blog(blog)
            .imageURL(url)
            .createdAt(Instant.now())
            .modifiedAt(Instant.now())
            .build();
    }

    @Override
    @Transactional
    public void deletePost(Long postId) {
        Post post = getPostById(postId);
        cloudinaryService.deleteFile(post.getImageURL());
        postRepository.delete(post);
    }

    public Post getPostById(Long postId) {
        return postRepository.findById(postId)
            .orElseThrow(() -> new PostNotFoundException("Post not found"));
    }
}
