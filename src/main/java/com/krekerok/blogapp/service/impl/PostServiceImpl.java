package com.krekerok.blogapp.service.impl;

import com.krekerok.blogapp.dto.request.PostRequestDto;
import com.krekerok.blogapp.dto.request.PostUpdateRequestDto;
import com.krekerok.blogapp.dto.response.PostResponseDto;
import com.krekerok.blogapp.entity.Blog;
import com.krekerok.blogapp.entity.Post;
import com.krekerok.blogapp.exception.sucurity.ForbiddingException;
import com.krekerok.blogapp.exception.data.PostNotFoundException;
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
import org.springframework.web.multipart.MultipartFile;

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
    public void deletePost(Long postId, String jwt) {
        Post post = findPostByPostId(postId);
        if (appUserService.checkingForDataCompliance(post.getBlog().getBlogId(), jwt)) {
            cloudinaryService.deleteFile(post.getImageURL());
            postRepository.delete(post);
        } else {
            throw new ForbiddingException("The user can update only his post.");
        }
    }

    @Override
    public PostResponseDto getPost(Long postId) {
        Post post = findPostByPostId(postId);
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
        Post post = findPostByPostId(postId);
        if (appUserService.checkingForDataCompliance(post.getBlog().getBlogId(), jwt)){
            post.setHeader(postUpdateRequestDto.getHeader());
            post.setText(postUpdateRequestDto.getText());
            post.setModifiedAt(Instant.now());
            Post updatedPost = postRepository.save(post);

            return PostMapper.INSTANCE.toPostResponseDto(updatedPost);
        } else {
            throw new ForbiddingException("The user can update only his post.");
        }
    }

    @Override
    @Transactional
    public PostResponseDto updatePostImage(Long postId, MultipartFile imageFile, String jwt) {
        Post post = findPostByPostId(postId);
        if (appUserService.checkingForDataCompliance(post.getBlog().getBlogId(), jwt)){

            cloudinaryService.deleteFile(post.getImageURL());

            String url =  cloudinaryService.uploadFile(imageFile);
            post.setImageURL(url);
            post.setModifiedAt(Instant.now());
            Post updatedPost = postRepository.save(post);

            return PostMapper.INSTANCE.toPostResponseDto(updatedPost);
        } else {
            throw new ForbiddingException("The user can update only his post.");
        }
    }


    @Override
    public Post findPostByPostId(Long postId) {
        return postRepository.findById(postId)
            .orElseThrow(() -> new PostNotFoundException("Post not found"));
    }

    @Override
    public boolean existsByPostId(Long postId) {
        return postRepository.existsById(postId);
    }
}
