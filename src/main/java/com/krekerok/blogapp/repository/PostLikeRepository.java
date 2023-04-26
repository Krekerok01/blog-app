package com.krekerok.blogapp.repository;

import com.krekerok.blogapp.entity.PostLike;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PostLikeRepository  extends JpaRepository<PostLike, Long> {

    boolean existsByAppUserIdAndPostId(long appUserId, long postId);

    void deleteByAppUserIdAndPostId(Long userId, long postId);
}
