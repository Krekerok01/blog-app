package com.krekerok.blogapp.service;

public interface PostLikeService {

    boolean putLikeOrRemoveItFromThePost(long postId, String jwt);
}
