package com.krekerok.blogapp.service;

public interface PostLikeService {

    boolean likeOrDislikePost(long postId, String jwt);
}
