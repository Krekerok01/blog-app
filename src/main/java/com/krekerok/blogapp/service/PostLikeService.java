package com.krekerok.blogapp.service;

public interface PostLikeService {

    boolean likeOrDislikePost(long appUserId, long postId);

}
