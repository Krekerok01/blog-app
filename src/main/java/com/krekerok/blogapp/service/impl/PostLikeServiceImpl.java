package com.krekerok.blogapp.service.impl;

import com.krekerok.blogapp.entity.AppUser;
import com.krekerok.blogapp.entity.PostLike;
import com.krekerok.blogapp.exception.PostNotFoundException;
import com.krekerok.blogapp.repository.PostLikeRepository;
import com.krekerok.blogapp.service.AppUserService;
import com.krekerok.blogapp.service.PostLikeService;
import com.krekerok.blogapp.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class PostLikeServiceImpl implements PostLikeService {

    @Autowired
    private AppUserService appUserService;
    @Autowired
    private PostService postService;
    @Autowired
    private PostLikeRepository postLikeRepository;

    @Transactional
    @Override
    public boolean likeOrDislikePost(long postId, String jwt) {

        AppUser appUser = appUserService.findAppUserByUsernameFromJWT(jwt);

        if (postService.existsByPostId(postId)){
            if (!postLikeRepository.existsByAppUserIdAndPostId(appUser.getUserId(), postId)){
                PostLike postLike = PostLike.builder()
                        .postId(postId)
                        .appUserId(appUser.getUserId())
                        .build();
                postLikeRepository.save(postLike);
                return true;
            } else {
                postLikeRepository.deleteByAppUserIdAndPostId(appUser.getUserId(), postId);
                return false;
            }

        }
        throw new PostNotFoundException("Post not found");
    }

}
