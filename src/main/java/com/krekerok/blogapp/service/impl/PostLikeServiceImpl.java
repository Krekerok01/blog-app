package com.krekerok.blogapp.service.impl;

import com.krekerok.blogapp.entity.PostLike;
import com.krekerok.blogapp.repository.PostLikeRepository;
import com.krekerok.blogapp.service.AppUserService;
import com.krekerok.blogapp.service.PostLikeService;
import com.krekerok.blogapp.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PostLikeServiceImpl implements PostLikeService {

    @Autowired
    private AppUserService appUserService;
    @Autowired
    private PostService postService;
    @Autowired
    private PostLikeRepository postLikeRepository;

    @Override
    public boolean likeOrDislikePost(long appUserId, long postId) {

        if (appUserService.existsByUserId(appUserId) && postService.existsByPostId(postId)){
            if (!postLikeRepository.existsByAppUserIdAndPostId(appUserId, postId)){
                PostLike postLike = PostLike.builder()
                        .postId(postId)
                        .appUserId(appUserId)
                        .build();
                postLikeRepository.save(postLike);
                return true;
            }
            return false;
        }
        throw new RuntimeException("Access exception");
    }

}
