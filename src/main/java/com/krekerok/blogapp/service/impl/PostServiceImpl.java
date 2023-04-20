package com.krekerok.blogapp.service.impl;

import com.krekerok.blogapp.dto.requests.PostRequestDto;
import com.krekerok.blogapp.dto.requests.PostUpdateRequestDto;
import com.krekerok.blogapp.dto.responses.PostResponseDto;
import com.krekerok.blogapp.entity.Blog;
import com.krekerok.blogapp.entity.Post;
import com.krekerok.blogapp.exception.NoBlogIdMatchException;
import com.krekerok.blogapp.exception.PostNotFoundException;
import com.krekerok.blogapp.mapper.PostMapper;
import com.krekerok.blogapp.repository.PostRepository;
import com.krekerok.blogapp.service.AppUserService;
import com.krekerok.blogapp.service.BlogService;
import com.krekerok.blogapp.service.CloudinaryService;
import com.krekerok.blogapp.service.PostService;
import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;
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
    @Autowired
    private AppUserService appUserService;

    @Transactional
    @Override
    public PostResponseDto createPost(Long blogId, PostRequestDto postRequestDto) {
        Blog blog = blogService.findBlogById(blogId);
        String url =  cloudinaryService.uploadFile(postRequestDto.getImageFile());
        Post post = postRepository.save(getPostFromPostRequestDto(blog, url, postRequestDto));
        return PostMapper.INSTANCE.toPostResponseDto(post);
    }

    private Post getPostFromPostRequestDto(Blog blog, String url, PostRequestDto postRequestDto){
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

    @Override
    public PostResponseDto getPost(Long postId) {
        Post post = getPostById(postId);
        return PostMapper.INSTANCE.toPostResponseDto(post);
    }

    @Override
    public List<PostResponseDto> getAllPostsByBlog(Blog blog) {
        List<Post> posts = postRepository.findAllByBlog(blog);
        if (!posts.isEmpty()){
            return posts.stream()
                .map(post -> PostMapper.INSTANCE.toPostResponseDto(post))
                .collect(Collectors.toList());
        } else {
            return null;
        }
    }

    @Override
    public PostResponseDto updatePostTextInfo(Long postId,
        PostUpdateRequestDto postUpdateRequestDto, String jwt) {
        Post post = getPostById(postId);
        if (appUserService.checkingForDataCompliance(post.getBlog().getBlogId(), jwt)){
            post.setHeader(postUpdateRequestDto.getHeader());
            post.setText(postUpdateRequestDto.getText());
            post.setModifiedAt(Instant.now());
            Post updatedPost = postRepository.save(post);

            return PostMapper.INSTANCE.toPostResponseDto(updatedPost);
        } else {
            throw new NoBlogIdMatchException("Invalid post id");
        }


    }

    public Post getPostById(Long postId) {
        return postRepository.findById(postId)
            .orElseThrow(() -> new PostNotFoundException("Post not found"));
    }
}
