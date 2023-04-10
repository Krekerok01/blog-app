package com.krekerok.blogapp.service.impl;

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
import org.springframework.web.multipart.MultipartFile;

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
    public Long createPost(Long blogId, MultipartFile file, String header, String text){
        Blog blog = blogService.findBlogById(blogId);
        String url =  cloudinaryService.uploadFile(file);
        Post post = postRepository.save(Post.builder()
            .header(header)
            .text(text)
            .blog(blog)
            .imageURL(url)
            .createdAt(Instant.now())
            .modifiedAt(Instant.now())
            .build());
        return post.getPostId();
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
