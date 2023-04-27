package com.krekerok.blogapp.service.impl;

import com.krekerok.blogapp.entity.AppUser;
import com.krekerok.blogapp.entity.Post;
import com.krekerok.blogapp.entity.PostLike;
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
        Post post = postService.findPostByPostId(postId);

        if (!postLikeRepository.existsByAppUserIdAndPostId(appUser.getUserId(), post.getPostId())){
            PostLike postLike = buildPostLike(post.getPostId(), appUser.getUserId());
            postLikeRepository.save(postLike);
            return true;
        } else {
            postLikeRepository.deleteByAppUserIdAndPostId(appUser.getUserId(), post.getPostId());
            return false;
        }
    }

    private PostLike buildPostLike(long postId, long appUserId) {
        return PostLike.builder()
            .postId(postId)
            .appUserId(appUserId)
            .build();
    }
}
